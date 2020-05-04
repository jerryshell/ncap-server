package cn.jerryshell.emotion.admin.service;

import cn.jerryshell.emotion.admin.entity.Task;
import com.baomidou.mybatisplus.extension.service.IService;

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
