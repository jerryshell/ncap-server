package cn.jerryshell.emotion.admin.entity;

import cn.jerryshell.emotion.admin.entity.dto.CommentSpiderTaskResponseComment;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    // 新闻编号
    private String newsId;
    // 评论内容
    private String content;
    // 正面评论概率
    private double p;
    // 负面评论概率
    private double n;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createDateTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateDateTime;

    public static Comment from(String newsId, CommentSpiderTaskResponseComment commentSpiderTaskResponseComment) {
        Comment comment = new Comment();
        comment.setId(commentSpiderTaskResponseComment.getCommentId());
        comment.setNewsId(newsId);
        comment.setContent(commentSpiderTaskResponseComment.getContent());
        return comment;
    }

    public static List<Comment> from(String newsId, List<CommentSpiderTaskResponseComment> commentSpiderTaskResponseCommentList) {
        return commentSpiderTaskResponseCommentList
                .parallelStream()
                .map(commentSpiderTaskResponseComment -> from(newsId, commentSpiderTaskResponseComment))
                .collect(Collectors.toList());
    }
}
