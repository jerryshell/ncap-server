package cn.jerryshell.emotion.admin.controller;

import cn.jerryshell.emotion.admin.entity.Comment;
import cn.jerryshell.emotion.admin.service.CommentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@CrossOrigin
@RestController
public class CommentController {
    @Resource
    private CommentService commentService;

    @GetMapping("/comment/listByNewsId/{newsId}/{pageNum}/{pageSize}")
    public Page<Comment> listByNewsId(
            @PathVariable String newsId,
            @PathVariable long pageNum,
            @PathVariable long pageSize
    ) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(true, "news_id", newsId);
        return commentService.page(new Page<>(pageNum, pageSize), queryWrapper);
    }

    @GetMapping("/comment/listOrderByUpdateDateTimeDESC")
    public List<Comment> listOrderByUpdateDateTimeDESC() {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("update_date_time");
        queryWrapper.last("limit 50");
        List<Comment> result = commentService.list(queryWrapper);
        log.info("result {}", result);

        return result;
    }

    @GetMapping("/comment/countPAndN")
    public Map<String, Object> countPAndN() {
        LocalDateTime startDateTime = LocalDateTime.now()
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
        log.info("startDateTime {}", startDateTime);

        LocalDateTime endDateTime = LocalDateTime.now()
                .withHour(23)
                .withMinute(59)
                .withSecond(59)
                .withNano(999999999);
        log.info("endDateTime {}", endDateTime);

        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("p", 50);
        queryWrapper.between("update_date_time", startDateTime, endDateTime);
        int pCount = commentService.count(queryWrapper);
        log.info("pCount {}", pCount);

        queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("n", 50);
        queryWrapper.between("update_date_time", startDateTime, endDateTime);
        int nCount = commentService.count(queryWrapper);
        log.info("nCount {}", nCount);

        Map<String, Object> result = new HashMap<>();
        result.put("pCount", pCount);
        result.put("nCount", nCount);
        return result;
    }
}
