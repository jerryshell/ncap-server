package io.github.jerryshell.ncap.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.jerryshell.ncap.admin.entity.Task;

public interface TaskService extends IService<Task> {
    long countByNewsId(String newsId);

    int updateStatusById(
            String id,
            int status
    );

    int updatePCountAndNCountAndProgressById(
            String id,
            int pCount,
            int nCount,
            int progress
    );
}
