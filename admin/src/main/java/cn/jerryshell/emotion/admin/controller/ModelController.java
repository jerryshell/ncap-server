package cn.jerryshell.emotion.admin.controller;

import cn.hutool.http.HttpRequest;
import cn.jerryshell.emotion.admin.entity.dto.ModelTest;
import cn.jerryshell.emotion.admin.entity.dto.ModelUpdate;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class ModelController {
    @Value("${emotion.analyse.server}")
    private String analyseServer;
    @Value("${emotion.analyse.server.token}")
    private String analyseServerToken;

    @GetMapping("/model/list/modelFilename")
    public String listModelFilename() {
        return HttpRequest.get(String.format(
                "%s/list/model_filename",
                analyseServer
        ))
                .execute()
                .body();
    }

    @PostMapping("/model/update")
    public String update(@Valid @RequestBody ModelUpdate modelUpdate) {
        log.info("{}", modelUpdate);

        Map<String, Object> updateModelFilenameData = new HashMap<>();
        updateModelFilenameData.put("token", "Super@dmin");
        updateModelFilenameData.put("model_filename", modelUpdate.getModelFilename());
        log.info("updateModelFilenameData {}", updateModelFilenameData);

        String updateModelFilenameResult = HttpRequest.post(String.format(
                "%s/modelReload",
                analyseServer
        ))
                .body(JSON.toJSONString(updateModelFilenameData))
                .execute()
                .body();
        log.info("updateModelFilenameResult {}", updateModelFilenameResult);

        Map<String, Object> updateRealTimeTuning = new HashMap<>();
        updateRealTimeTuning.put("token", "Super@dmin");
        updateRealTimeTuning.put("key", "set trainStatus.realTimeTuning");
        updateRealTimeTuning.put(
                "value",
                modelUpdate.getRealTimeTuning() ? "open" : "close"
        );
        log.info("updateRealTimeTuning {}", updateRealTimeTuning);

        String updateRealTimeTuningResult = HttpRequest.post(String.format(
                "%s/zero",
                analyseServer
        ))
                .body(JSON.toJSONString(updateRealTimeTuning))
                .execute()
                .body();
        log.info("updateRealTimeTuningResult {}", updateRealTimeTuningResult);

        return "{\"ok\":true}";
    }

    @PostMapping("/model/test")
    public String test(@Valid @RequestBody ModelTest modelTest) {
        log.info("{}", modelTest);

        Map<String, Object> modelTestData = new HashMap<>();
        modelTestData.put("sentence", modelTest.getSentence());
        modelTestData.put("token", analyseServerToken);
        log.info("{}", modelTestData);

        String response = HttpRequest.post(analyseServer)
                .body(JSON.toJSONString(modelTestData))
                .execute()
                .body();
        log.info("{}", response);

        return response;
    }
}
