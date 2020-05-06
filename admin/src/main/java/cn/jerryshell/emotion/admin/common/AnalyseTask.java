package cn.jerryshell.emotion.admin.common;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.jerryshell.emotion.admin.entity.Comment;
import cn.jerryshell.emotion.admin.entity.Task;
import cn.jerryshell.emotion.admin.entity.dto.AnalyseResponse;
import cn.jerryshell.emotion.admin.service.CommentService;
import cn.jerryshell.emotion.admin.service.TaskService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class AnalyseTask implements Runnable {
    private final Task task;
    private final TaskService taskService;
    private final List<Comment> commentList;
    private final CommentService commentService;
    private final String analyseServer;

    public AnalyseTask(
            Task task,
            TaskService taskService,
            List<Comment> commentList,
            CommentService commentService,
            String analyseServer
    ) {
        this.task = task;
        this.taskService = taskService;
        this.commentList = commentList;
        this.commentService = commentService;
        this.analyseServer = analyseServer;
    }

    @Override
    public void run() {
        Map<String, Object> data = new HashMap<>();
        for (int i = 0; i < commentList.size(); i++) {
            Comment comment = commentList.get(i);
            log.info(comment.toString());

            // 计算和更新任务进度
            double progress = (i + 1.0) / commentList.size() * 100;
            log.info("分析进度 {}/{} {}%", i + 1, commentList.size(), progress);
            task.setProgress((int) progress);

            // 组装情感分析接口参数
            data.put("sentence", comment.getContent());
            data.put("token", "Super@dmin");
            log.info(data.toString());

            // 调用情感分析接口
            String response = null;
            try {
                response = HttpRequest.post(analyseServer)
                        .body(JSON.toJSONString(data))
                        .execute()
                        .body();
                log.info(response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (StrUtil.isBlank(response)) {
                log.warn("调用情感分析接口发生错误");
                return;
            }

            // 解析情感分析接口响应
            AnalyseResponse analyseResponse = JSON.parseObject(response, AnalyseResponse.class);
            log.info(analyseResponse.toString());

            // 如果不能成功直接跳到下一个评论
            if (!analyseResponse.isOk()) {
                continue;
            }

            // 保存分析结果
            double p = analyseResponse.getP();
            double n = analyseResponse.getN();
            comment.setP(p);
            comment.setN(n);
            commentService.saveOrUpdate(comment);

            // 更新评论计数，如果正负面概率不大于 60，表示该评论可能为中立评论，跳过计数
            if (p >= 60) {
                task.setPCount(task.getPCount() + 1);
            } else if (n >= 60) {
                task.setNCount(task.getNCount() + 1);
            }
            log.info("正面评论计数 {} 负面评论计数 {}", task.getPCount(), task.getNCount());

            // 计算当前负面新闻概率
            double countSum = task.getPCount() + task.getNCount();
            if (countSum > 0) {
                log.info("负面新闻概率 {}}%", (task.getNCount() / countSum * 100));
            }

            // 按阶段更新数据到数据库中
            if (task.getProgress() % 5 == 0) {
                log.info("按阶段更新数据到数据库中");
                taskService.updatePCountAndNCountAndProgressById(task.getId(), task.getPCount(), task.getNCount(), task.getProgress());
            }
        }

        // 遍历结束，将任务标记为已完成
        task.setStatus(Task.STATUS_COMPLETE);
        taskService.updateStatusById(task.getId(), task.getStatus());
    }
}
