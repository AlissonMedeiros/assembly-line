package com.medeiros.assemblyline.service;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.medeiros.assemblyline.model.AssemblyLinePool;
import com.medeiros.assemblyline.model.Task;
import com.medeiros.assemblyline.parser.TaskParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class AssemblyLineAdjuster {

    private final AssemblyLinePoolPrinter printer;
    private final LineBalancer balancer;

    public AssemblyLineAdjuster(AssemblyLinePoolPrinter printer, LineBalancer balancer) {
        this.printer = printer;
        this.balancer = balancer;
    }

    public List<String> ajust(int poolSize, List<Task> tasks) {
        AssemblyLinePool pool = balancer.balance(poolSize, tasks);
        return printer.print(pool);
    }

    public Resource ajust(Integer poolSize, InputStream inputStream) throws IOException {
        String tasksFile = CharStreams.toString(new InputStreamReader(inputStream, Charsets.UTF_8));
        List<Task> tasks = TaskParser.parse(Arrays.asList(tasksFile.split("\n")));
        AssemblyLinePool pool = balancer.balance(poolSize, tasks);
        List<String> response = printer.print(pool);
        StringBuilder builder = new StringBuilder();
        response.forEach(s -> builder.append(s + "\n"));
        return new ByteArrayResource(builder.toString().getBytes());
    }

}
