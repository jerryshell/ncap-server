package cn.jerryshell.emotion.admin.service.impl;

import cn.jerryshell.emotion.admin.entity.Comment;
import cn.jerryshell.emotion.admin.mapper.CommentMapper;
import cn.jerryshell.emotion.admin.service.CommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
}
