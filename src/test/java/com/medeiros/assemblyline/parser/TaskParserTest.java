package com.medeiros.assemblyline.parser;

import com.medeiros.assemblyline.model.Task;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class TaskParserTest {

    @Test
    public void whenHasTimedTaskThenParser() {
        Task task = TaskParser.parserLine("Cutting of steel sheets 60min");
        Assertions.assertThat(task.getName()).isEqualTo("Cutting of steel sheets");
        Assertions.assertThat(task.getMinutes()).isEqualTo(60L);
    }

    @Test
    public void whenHasLineWithOneWordNameThenParser() {
        Task task = TaskParser.parserLine("Cutting 1506min");
        Assertions.assertThat(task.getName()).isEqualTo("Cutting");
        Assertions.assertThat(task.getMinutes()).isEqualTo(1506L);
    }

    @Test
    public void whenHasTimeWithoutSpaceThenParser() {
        Task task = TaskParser.parserLine("Cutting1min");
        Assertions.assertThat(task.getName()).isEqualTo("Cutting");
        Assertions.assertThat(task.getMinutes()).isEqualTo(1L);
    }

    @Test
    public void whenHasNotTimeAndIsMaintenanceThenParser() {
        Task task = TaskParser.parserLine("Assembly line cooling - maintenance");
        Assertions.assertThat(task.getName()).isEqualTo("Assembly line cooling - maintenance");
        Assertions.assertThat(task.getMinutes()).isEqualTo(5L);
    }

    @Test
    public void whenHasEmptyThenParser() {
        Task task = TaskParser.parserLine("");
        Assertions.assertThat(task.getName()).isEqualTo("unnamed");
        Assertions.assertThat(task.getMinutes()).isEqualTo(0L);
    }

    @Test
    public void whenHasOnlyTimeThenParser() {
        Task task = TaskParser.parserLine("5min");
        Assertions.assertThat(task.getName()).isEqualTo("unnamed");
        Assertions.assertThat(task.getMinutes()).isEqualTo(5L);
    }
}