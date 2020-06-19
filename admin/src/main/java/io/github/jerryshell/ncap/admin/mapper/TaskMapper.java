package io.github.jerryshell.ncap.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.jerryshell.ncap.admin.entity.Task;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;

@Mapper
public interface TaskMapper extends BaseMapper<Task> {
    @Select("select count(*) from task where news_id=#{newsId}")
    long countByNewsId(String newsId);

    @Update("update task set " +
            "status=#{status}, " +
            "update_date_time=#{updateDateTime} " +
            "where id=#{id}")
    int updateStatusById(
            String id,
            int status,
            LocalDateTime updateDateTime
    );

    @Update("update task set " +
            "p_count=#{pCount}, " +
            "n_count=#{nCount}, " +
            "progress=#{progress}, " +
            "update_date_time=#{updateDateTime} " +
            "where id=#{id}")
    int updatePCountAndNCountAndProgressById(
            String id,
            int pCount,
            int nCount,
            int progress,
            LocalDateTime updateDateTime
    );
}
