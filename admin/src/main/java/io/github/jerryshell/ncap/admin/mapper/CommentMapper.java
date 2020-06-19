package io.github.jerryshell.ncap.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.jerryshell.ncap.admin.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
}
