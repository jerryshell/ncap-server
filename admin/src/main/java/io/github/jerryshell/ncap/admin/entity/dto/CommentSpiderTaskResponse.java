package io.github.jerryshell.ncap.admin.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentSpiderTaskResponse {
    private Boolean ok;
    private String title;
    private List<CommentSpiderTaskResponseComment> commentList;
}
