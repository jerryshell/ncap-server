package io.github.jerryshell.ncap.admin.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModelUpdate {
    @NotBlank
    private String modelFilename;
    @NotNull
    private Boolean realTimeTuning;
}
