package cn.jerryshell.emotion.admin.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalyseResponse {
    private boolean ok;
    private double p;
    private double n;
}
