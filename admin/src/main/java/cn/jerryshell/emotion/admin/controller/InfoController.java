package cn.jerryshell.emotion.admin.controller;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class InfoController {

    @Value("${emotion.notify.server}")
    private String notifyServer;
    @Value("${emotion.analyse.server}")
    private String analyseServer;
    @Value("${emotion.spider.server}")
    private String spiderServer;

    @GetMapping("/info")
    public Map<String, Object> info() {
        Map<String, Object> result = new HashMap<>();

        result.put("server", getServerStatus());

        String spiderServerStatus = getSpiderServerStatus();
        result.put("spiderServer", JSONUtil.toBean(spiderServerStatus, Map.class));

        String analyseServerStatus = getAnalyseServerStatus();
        result.put("analyseServer", JSONUtil.toBean(analyseServerStatus, Map.class));

        return result;
    }

    private Map<String, Object> getServerStatus() {
        Map<String, Object> result = new HashMap<>();
        result.put("ok", true);
        result.put("notifyServer", notifyServer);
        result.put("analyseServer", analyseServer);
        result.put("spiderServer", spiderServer);
        return result;
    }

    private String getSpiderServerStatus() {
        String result = "{\"ok\":false}";
        try {
            result = HttpRequest.get(String.format("%s/info", spiderServer))
                    .timeout(1000)
                    .execute()
                    .body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private String getAnalyseServerStatus() {
        String result = "{\"ok\":false}";
        try {
            result = HttpRequest.get(String.format("%s/info", analyseServer))
                    .timeout(1000)
                    .execute()
                    .body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
