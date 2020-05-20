package cn.jerryshell.emotion.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    // 任务状态：采集进行中
    public static final int STATUS_SPIDER = 0;
    // 任务状态：采集失败
    public static final int STATUS_SPIDER_FAIL = 10;
    // 任务状态：评论分析
    public static final int STATUS_ANALYSE = 1;
    // 任务状态：分析失败
    public static final int STATUS_ANALYSE_FAIL = 11;
    // 任务状态：任务完成
    public static final int STATUS_COMPLETE = 2;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    // 新闻编号
    private String newsId;
    // 新闻连接
    private String newsUrl;
    // 新闻标题
    private String newsTitle;
    // 状态
    private Integer status;
    // 正面评论计数
    private Integer pCount;
    // 负面评论计数
    private Integer nCount;
    // 进度
    private Integer progress;

    private LocalDateTime createDateTime;
    private LocalDateTime updateDateTime;
}
