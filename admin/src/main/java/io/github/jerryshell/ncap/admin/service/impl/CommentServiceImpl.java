package io.github.jerryshell.ncap.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.jerryshell.ncap.admin.entity.Comment;
import io.github.jerryshell.ncap.admin.mapper.CommentMapper;
import io.github.jerryshell.ncap.admin.service.CommentService;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
}
