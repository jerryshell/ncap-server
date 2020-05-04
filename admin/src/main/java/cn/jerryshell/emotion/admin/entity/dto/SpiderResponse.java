package cn.jerryshell.emotion.admin.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpiderResponse {
    private Boolean ok;
    private String title;
    private List<SpiderResponseComment> commentList;
}
