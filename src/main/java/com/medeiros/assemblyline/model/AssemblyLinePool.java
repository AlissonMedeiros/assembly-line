package com.medeiros.assemblyline.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AssemblyLinePool implements AutoCloseable {

    private List<AssemblyLine> pool = Collections.emptyList();

    private AssemblyLinePool() {

    }

    public static AssemblyLinePool open(int size) {
        AssemblyLinePool assemblyLinePool = new AssemblyLinePool();
        assemblyLinePool.pool = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            assemblyLinePool.pool.add(new AssemblyLine().open());
        }
        return assemblyLinePool;
    }

    public List<AssemblyLine> getPool() {
        return Collections.unmodifiableList(pool);
    }

    public AssemblyLine findOne() {
        if (pool.size() == 1) {
            return pool.get(0);
        }
        return pool.stream()
                .sorted(AssemblyLinePool::compare)
                .findFirst()
                .get();
    }

    @Override
    public void close() {
        pool.forEach(assemblyLine -> assemblyLine.close());
    }

    private static int compare(AssemblyLine assemblyLine, AssemblyLine assemblyLine1) {
        long size1 = assemblyLine1.getMorning().getSize() + assemblyLine1.getAfternoon().getSize();
        long size = assemblyLine.getMorning().getSize() + assemblyLine.getAfternoon().getSize();
        return Long.compare(size, size1);

    }
}
