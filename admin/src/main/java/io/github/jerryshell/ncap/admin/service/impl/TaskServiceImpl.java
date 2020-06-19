package io.github.jerryshell.ncap.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.jerryshell.ncap.admin.entity.Task;
import io.github.jerryshell.ncap.admin.mapper.TaskMapper;
import io.github.jerryshell.ncap.admin.service.TaskService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService {
    @Override
    public long countByNewsId(String newsId) {
        return baseMapper.countByNewsId(newsId);
    }

    @Override
    public int updateStatusById(String id, int status) {
        return baseMapper.updateStatusById(
                id,
                status,
                LocalDateTime.now()
        );
    }

    @Override
    public int updatePCountAndNCountAndProgressById(
            String id,
            int pCount,
            int nCount,
            int progress
    ) {
        return baseMapper.updatePCountAndNCountAndProgressById(
                id,
                pCount,
                nCount,
                progress,
                LocalDateTime.now()
        );
    }
}
