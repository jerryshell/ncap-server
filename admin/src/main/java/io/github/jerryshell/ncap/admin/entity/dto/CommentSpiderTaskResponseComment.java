package io.github.jerryshell.ncap.admin.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentSpiderTaskResponseComment {
    private String commentId;
    private String content;
}
