package com.medeiros.assemblyline.service;

import com.medeiros.assemblyline.model.AssemblyLinePool;
import com.medeiros.assemblyline.model.Task;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.Resource;

public class AssemblyLineAdjusterTest {

    @InjectMocks
    private AssemblyLineAdjuster assemblyLineAdjuster;
    @Mock
    private AssemblyLinePoolPrinter printer;
    @Mock
    private LineBalancer balancer;

    @BeforeEach
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void whenHasObjectThenReturnList() {
        List<Task> tasks = Arrays.asList(Task.builder()
                .name("a")
                .minutes(1L)
                .build());
        AssemblyLinePool pool = AssemblyLinePool.open(1);
        Mockito.when(balancer.balance(1, tasks)).thenReturn(pool);
        Mockito.when(printer.print(pool)).thenReturn(Arrays.asList("a"));
        List<String> result = assemblyLineAdjuster.ajust(1, tasks);
        Assertions.assertThat(result)
                .hasSize(1)
                .contains("a");
    }

    @Test
    public void whenHasFileThenReturnResource() throws IOException {
        InputStream stream = new ByteArrayInputStream("a 1min".getBytes());
        List<Task> tasks = Arrays.asList(Task.builder()
                .name("a")
                .minutes(1L)
                .build());
        AssemblyLinePool pool = AssemblyLinePool.open(1);
        Mockito.when(balancer.balance(1, tasks)).thenReturn(pool);
        Mockito.when(printer.print(pool)).thenReturn(Arrays.asList("a"));
        Resource result = assemblyLineAdjuster.ajust(1, stream);
        Assertions.assertThat(result.getInputStream())
                .hasContent("a");
    }

}