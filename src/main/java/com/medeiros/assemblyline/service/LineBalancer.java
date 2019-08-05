package com.medeiros.assemblyline.service;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.base.Preconditions;
import com.medeiros.assemblyline.model.AssemblyLine;
import com.medeiros.assemblyline.model.AssemblyLinePool;
import com.medeiros.assemblyline.model.Task;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class LineBalancer {

    public AssemblyLinePool balance(int poolSize, List<Task> tasks) {
        checkPool(poolSize);
        checkTasks(tasks);
        return adjust(poolSize, tasks);
    }

    private AssemblyLinePool adjust(int poolSize, List<Task> tasks) {
        try (AssemblyLinePool pool = AssemblyLinePool.open(poolSize)) {
            for (Task task : tasks) {
                AssemblyLine line = pool.findOne();
                line.add(task);
            }
            return pool;
        }
    }

    private void checkTasks(List<Task> tasks) {
        Preconditions.checkNotNull(tasks, "Tasks can't be null.");
    }

    private void checkPool(Integer pool) {
        checkArgument(pool > 0, "Pool can't be empty.");
    }
}
