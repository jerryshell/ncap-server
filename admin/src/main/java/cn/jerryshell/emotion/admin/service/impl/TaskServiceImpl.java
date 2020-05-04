package cn.jerryshell.emotion.admin.service.impl;

import cn.jerryshell.emotion.admin.entity.Task;
import cn.jerryshell.emotion.admin.mapper.TaskMapper;
import cn.jerryshell.emotion.admin.service.TaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
