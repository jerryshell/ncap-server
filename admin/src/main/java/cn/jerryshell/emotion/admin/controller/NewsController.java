package cn.jerryshell.emotion.admin.controller;

import cn.hutool.http.HttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class NewsController {
    @Value("${emotion.spider.server}")
    private String spiderServer;

    @GetMapping("/rankNews")
    String getRankNews() {
        return HttpRequest.get(String.format("%s/rankNews", spiderServer))
                .execute()
                .body();
    }
}
