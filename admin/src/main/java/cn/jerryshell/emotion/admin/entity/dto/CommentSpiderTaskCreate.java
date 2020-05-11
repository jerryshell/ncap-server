package cn.jerryshell.emotion.admin.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentSpiderTaskCreate {
    @NotBlank
    private String newsUrl;
}
