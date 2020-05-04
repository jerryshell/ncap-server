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
}
