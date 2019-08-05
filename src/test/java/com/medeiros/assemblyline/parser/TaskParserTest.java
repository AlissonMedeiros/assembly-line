package com.medeiros.assemblyline.parser;

import com.medeiros.assemblyline.model.Task;
import java.util.Arrays;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class TaskParserTest {

    public static final Task[] TASKS = {Task.builder().name("Cutting of steel sheets").minutes(60L).build()
            , Task.builder().name("Austenpera (Heat treatment)").minutes(30L).build()
            , Task.builder().name("Tempering sub-zero (Heat treatment)").minutes(45L).build()
            , Task.builder().name("Safety sensor assembly").minutes(60L).build()
            , Task.builder().name("Pieces washing").minutes(45L).build()
            , Task.builder().name("Axis calibration").minutes(30L).build()
            , Task.builder().name("Steel bearing assembly").minutes(45L).build()
            , Task.builder().name("Assembly line cooling - maintenance").minutes(5L).build()
            , Task.builder().name("Nitriding process").minutes(45L).build()
            , Task.builder().name("Injection subsystem assembly").minutes(60L).build()
            , Task.builder().name("Compliance check").minutes(30L).build()
            , Task.builder().name("Navigation subsystem assembly").minutes(60L).build()
            , Task.builder().name("Torque converter subsystem calibration").minutes(60L).build()
            , Task.builder().name("Left stabilizer bar alignment").minutes(30L).build()
            , Task.builder().name("Setup of lock and control device").minutes(45L).build()
            , Task.builder().name("Right stabilizer bar alignment").minutes(30L).build()
            , Task.builder().name("Seal installation").minutes(45L).build()
            , Task.builder().name("Application of decals").minutes(30L).build()};

    public static final Task[] TASKS_FULL = {Task.builder().name("Cutting of steel sheets").minutes(60L).build()
            , Task.builder().name("Austenpera (Heat treatment)").minutes(30L).build()
            , Task.builder().name("Tempering sub-zero (Heat treatment)").minutes(45L).build()
            , Task.builder().name("Safety sensor assembly").minutes(60L).build()
            , Task.builder().name("Pieces washing").minutes(45L).build()
            , Task.builder().name("Axis calibration").minutes(30L).build()
            , Task.builder().name("Steel bearing assembly").minutes(45L).build()
            , Task.builder().name("Assembly line cooling - maintenance").minutes(5L).build()
            , Task.builder().name("Nitriding process").minutes(45L).build()
            , Task.builder().name("Injection subsystem assembly").minutes(60L).build()
            , Task.builder().name("Compliance check").minutes(30L).build()
            , Task.builder().name("Navigation subsystem assembly").minutes(60L).build()
            , Task.builder().name("Torque converter subsystem calibration").minutes(60L).build()
            , Task.builder().name("Left stabilizer bar alignment").minutes(30L).build()
            , Task.builder().name("Setup of lock and control device").minutes(45L).build()
            , Task.builder().name("Right stabilizer bar alignment").minutes(30L).build()
            , Task.builder().name("Seal installation").minutes(45L).build()
            , Task.builder().name("Application of decals").minutes(30L).build()
            , Task.builder().name("Monitoring subsystem assembly").minutes(30L).build()};

    @Test
    public void whenHasListThenParser() {
        List<Task> tasks = TaskParser.parse(Arrays.asList("Cutting of steel sheets 60min",
                "Austenpera (Heat treatment) 30min",
                "Tempering sub-zero (Heat treatment) 45min",
                "Safety sensor assembly 60min",
                "Pieces washing 45min",
                "Axis calibration 30min",
                "Steel bearing assembly 45min",
                "Assembly line cooling - maintenance",
                "Nitriding process 45min",
                "Injection subsystem assembly 60min",
                "Compliance check 30min",
                "Navigation subsystem assembly 60min",
                "Torque converter subsystem calibration 60min",
                "Left stabilizer bar alignment 30min",
                "Setup of lock and control device 45min",
                "Right stabilizer bar alignment 30min",
                "Seal installation 45min",
                "Application of decals 30min",
                "Monitoring subsystem assembly 30min"));
        Assertions.assertThat(tasks).hasSize(19)
                .contains(TASKS_FULL);
    }

    @Test
    public void whenHasTimedTaskThenParser() {
        Task task = TaskParser.parse("Tempering sub-zero (Heat treatment) 45min");
        Assertions.assertThat(task.getName()).isEqualTo("Tempering sub-zero (Heat treatment)");
        Assertions.assertThat(task.getMinutes()).isEqualTo(45L);
    }

    @Test
    public void whenHasTimedTaskWithSpecialCharThenParser() {
        Task task = TaskParser.parse("Cutting of steel sheets 60min");
        Assertions.assertThat(task.getName()).isEqualTo("Cutting of steel sheets");
        Assertions.assertThat(task.getMinutes()).isEqualTo(60L);
    }

    @Test
    public void whenHasLineWithOneWordNameThenParser() {
        Task task = TaskParser.parse("Cutting 1506min");
        Assertions.assertThat(task.getName()).isEqualTo("Cutting");
        Assertions.assertThat(task.getMinutes()).isEqualTo(1506L);
    }

    @Test
    public void whenHasTimeWithoutSpaceThenParser() {
        Task task = TaskParser.parse("Cutting1min");
        Assertions.assertThat(task.getName()).isEqualTo("Cutting");
        Assertions.assertThat(task.getMinutes()).isEqualTo(1L);
    }

    @Test
    public void whenHasNotTimeAndIsMaintenanceThenParser() {
        Task task = TaskParser.parse("Assembly line cooling - maintenance");
        Assertions.assertThat(task.getName()).isEqualTo("Assembly line cooling - maintenance");
        Assertions.assertThat(task.getMinutes()).isEqualTo(5L);
    }

    @Test
    public void whenHasEmptyThenParser() {
        Task task = TaskParser.parse("");
        Assertions.assertThat(task.getName()).isEqualTo("unnamed");
        Assertions.assertThat(task.getMinutes()).isEqualTo(0L);
    }

    @Test
    public void whenHasOnlyTimeThenParser() {
        Task task = TaskParser.parse("5min");
        Assertions.assertThat(task.getName()).isEqualTo("unnamed");
        Assertions.assertThat(task.getMinutes()).isEqualTo(5L);
    }

    @Test
    public void whenHasNoTimedTaskThenParser() {
        Task task = TaskParser.parse("Cutting of steel sheets");
        Assertions.assertThat(task.getName()).isEqualTo("Cutting of steel sheets");
        Assertions.assertThat(task.getMinutes()).isEqualTo(0L);
    }

}