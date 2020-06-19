package io.github.jerryshell.ncap.admin.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalyseTaskCreate {
    @NotBlank
    private String taskId;
}
