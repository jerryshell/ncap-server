package io.github.jerryshell.ncap.admin.controller;

import cn.hutool.core.util.IdUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {
    @PostMapping("/login")
    public Map<String, String> login() {
        Map<String, String> result = new HashMap<>();
        result.put("uuid", IdUtil.fastSimpleUUID());
        result.put("token", IdUtil.fastSimpleUUID());
        result.put("name", "Admin");
        return result;
    }
}
