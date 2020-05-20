package cn.jerryshell.emotion.admin.controller;

import cn.hutool.http.HttpRequest;
import cn.jerryshell.emotion.admin.common.AnalyseTask;
import cn.jerryshell.emotion.admin.common.R;
import cn.jerryshell.emotion.admin.entity.Comment;
import cn.jerryshell.emotion.admin.entity.Task;
import cn.jerryshell.emotion.admin.entity.dto.AnalyseTaskCreate;
import cn.jerryshell.emotion.admin.entity.dto.CommentSpiderTaskCreate;
import cn.jerryshell.emotion.admin.entity.dto.CommentSpiderTaskResponse;
import cn.jerryshell.emotion.admin.entity.dto.CommentSpiderTaskResponseComment;
import cn.jerryshell.emotion.admin.service.CommentService;
import cn.jerryshell.emotion.admin.service.TaskService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class TaskController {
    @Resource
    private TaskService taskService;
    @Resource
    private CommentService commentService;

    @Value("${emotion.notify.server}")
    private String notifyServer;
    @Value("${emotion.analyse.server}")
    private String analyseServer;
    @Value("${emotion.spider.server}")
    private String spiderServer;

    @GetMapping("/task/list/{pageNum}/{pageSize}")
    public R<Page<Task>> list(@PathVariable int pageNum, @PathVariable int pageSize) {
        QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_date_time");
        Page<Task> page = taskService.page(new Page<>(pageNum, pageSize), queryWrapper);
        return R.ok(page);
    }

    @GetMapping("/task/list/running")
    public R<List<Task>> listByRunning() {
        QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_date_time");
        queryWrapper.lt("progress", 100);
        return R.ok(taskService.list(queryWrapper));
    }

    @GetMapping("/task/getById/{id}")
    public R<Task> getById(@PathVariable String id) {
        return R.ok(taskService.getById(id));
    }

    @GetMapping("/task/countByNewsId/{newsId}")
    public R<Long> countByNewsId(@PathVariable String newsId) {
        long count = taskService.countByNewsId(newsId);
        return R.ok(count);
    }

    @PostMapping("/task/create")
    public R<?> createTask(
            @Valid @RequestBody CommentSpiderTaskCreate commentSpiderTaskCreate
    ) {
        log.info("{}", commentSpiderTaskCreate);

        // 提取 newsId
        @NotBlank String newsUrl = commentSpiderTaskCreate.getNewsUrl();
        String[] split = newsUrl.split("/");
        log.info(Arrays.toString(split));
        String html = split[split.length - 1];
        log.info(html);
        String newsId = html.replace(".html", "");
        log.info(newsId);

        // 创建任务
        Task task = new Task();
        task.setNewsId(newsId);
        task.setNewsUrl(newsUrl);
        task.setNewsTitle("未知，等待采集");
        task.setPCount(0);
        task.setNCount(0);
        task.setProgress(0);
        task.setStatus(Task.STATUS_SPIDER);
        task.setCreateDateTime(LocalDateTime.now());
        taskService.save(task);

        // 启动采集任务
        startSpiderTask(task);

        return R.ok();
    }

    @PostMapping("/task/rebootTask/{taskId}")
    public R<?> rebootTask(
            @PathVariable String taskId
    ) {
        log.info("{}", taskId);

        // 校验 taskId
        Task task = taskService.getById(taskId);
        log.info("{}", task);
        if (task == null) {
            return R.notfound();
        }

        // 重置任务数据
        task.setPCount(0);
        task.setNCount(0);
        task.setProgress(0);
        task.setStatus(Task.STATUS_SPIDER);
        task.setUpdateDateTime(LocalDateTime.now());
        taskService.updateById(task);

        // 删除旧评论
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("news_id", task.getNewsId());
        commentService.remove(queryWrapper);

        // 启动采集任务
        startSpiderTask(task);

        return R.ok();
    }

    private void startSpiderTask(Task task) {
        String notifyUrl = String.format(
                "%s/task/spiderNotify/%s",
                notifyServer,
                task.getId()
        );
        Map<String, String> data = new HashMap<>();
        data.put("news_id", task.getNewsId());
        data.put("news_url", task.getNewsUrl());
        data.put("notify_url", notifyUrl);
        String response = HttpRequest.post(String.format(
                "%s/create/commentSpiderTask",
                spiderServer
        ))
                .body(JSON.toJSONString(data))
                .execute()
                .body();
        log.info(response);
    }

    @PostMapping("/task/spiderNotify/{taskId}")
    public R<?> spiderNotify(
            @PathVariable String taskId,
            @RequestBody CommentSpiderTaskResponse commentSpiderTaskResponse
    ) {
        log.info(taskId);
        log.info("{}", commentSpiderTaskResponse);

        // 获取该任务信息
        Task task = taskService.getById(taskId);
        if (task == null) {
            log.error("该任务已被删除");
            return R.ok();
        }

        // 更新任务状态
        if (!commentSpiderTaskResponse.getOk()) {
            task.setNewsTitle(commentSpiderTaskResponse.getTitle());
            task.setStatus(Task.STATUS_SPIDER_FAIL);
            task.setUpdateDateTime(LocalDateTime.now());
            taskService.updateById(task);
            log.error("采集失败 {}", task);
            return R.ok();
        }
        task.setNewsTitle(commentSpiderTaskResponse.getTitle());
        task.setPCount(0);
        task.setNCount(0);
        task.setProgress(0);
        task.setStatus(Task.STATUS_ANALYSE);
        task.setUpdateDateTime(LocalDateTime.now());
        taskService.updateById(task);

        // 提取评论列表
        List<CommentSpiderTaskResponseComment> commentSpiderTaskResponseCommentList = commentSpiderTaskResponse.getCommentList();
        log.info("{}", commentSpiderTaskResponseCommentList);

        // 评论去重
        commentSpiderTaskResponseCommentList = commentSpiderTaskResponseCommentList.parallelStream().distinct().collect(Collectors.toList());

        // 根据 spiderResponseCommentList 生成 commentList
        List<Comment> commentList = Comment.from(task.getNewsId(), commentSpiderTaskResponseCommentList);

        log.info("{}", commentList);

        // 保存评论
        commentService.saveOrUpdateBatch(commentList);

        // 启动分析任务
        startAnalyseTask(task, commentList);

        return R.ok();
    }

    @PostMapping("/task/createAnalyseTask")
    public R<?> createAnalyseTask(
            @Valid @RequestBody AnalyseTaskCreate analyseTaskCreate
    ) {
        log.info(analyseTaskCreate.toString());

        @NotBlank String taskId = analyseTaskCreate.getTaskId();
        Task task = taskService.getById(taskId);
        log.info("{}", task);
        if (task == null) {
            return R.error("task == null");
        }

        // 更新任务状态
        task.setPCount(0);
        task.setNCount(0);
        task.setProgress(0);
        task.setStatus(Task.STATUS_ANALYSE);
        task.setUpdateDateTime(LocalDateTime.now());
        taskService.updateById(task);

        // 启动分析任务
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("news_id", task.getNewsId());
        List<Comment> commentList = commentService.list(queryWrapper);
        startAnalyseTask(task, commentList);

        return R.ok();
    }

    private void startAnalyseTask(Task task, List<Comment> commentList) {
        AnalyseTask analyseTask = new AnalyseTask(
                task,
                taskService,
                commentList,
                commentService,
                analyseServer
        );
        Thread analyseTaskThread = new Thread(analyseTask);
        analyseTaskThread.start();
        log.info("analyseTaskThread.start()");
    }
}
