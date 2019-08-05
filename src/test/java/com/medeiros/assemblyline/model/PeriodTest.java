package com.medeiros.assemblyline.model;

import java.time.LocalTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class PeriodTest {

    @Test
    public void whenIsEmptyThenReturnTrue() {
        Period period = PeriodBuilder.morning();
        Assertions.assertThat(period.canAdd(Task.builder()
                .minutes(60L)
                .build())).isTrue();
    }

    @Test
    public void whenDurationIsGreaterThanThePoolThenReturnFalse() {
        Period period = PeriodBuilder.morning();
        Assertions.assertThat(period.canAdd(Task.builder()
                .minutes(10000L)
                .build())).isFalse();
    }

    @Test
    public void whenDurationIsEqualThenReturnFalse() {
        Period period = PeriodBuilder.morning();
        Assertions.assertThat(period.canAdd(Task.builder()
                .minutes(180L)
                .build())).isTrue();
    }

    @Test
    public void whenAddTaskThenCreateStep() {
        Period period = PeriodBuilder.morning();
        period.add(Task.builder()
                .minutes(60L)
                .name("new task")
                .build());
        Step step = period.getSteps().get(0);
        Assertions.assertThat(step).isNotNull();
        Assertions.assertThat(step.getName()).isEqualTo("new task");
        Assertions.assertThat(step.getDuration()).isEqualTo(60L);
        Assertions.assertThat(step.getStart()).isEqualTo(LocalTime.of(9, 00));
        Assertions.assertThat(step.getEnd()).isEqualTo(LocalTime.of(10, 00));
        Assertions.assertThat(period.getSize()).isEqualTo(60L);
        Assertions.assertThat(period.getSteps()).hasSize(1)
                .contains(step);
    }

    @Test
    public void whenAddManyTasksThenIncreaseSize() {
        Period period = PeriodBuilder.morning();
        period.add(Task.builder()
                .minutes(60L)
                .name("new task")
                .build());
        period.add(Task.builder()
                .minutes(45L)
                .name("Gym")
                .build());
        Step task = period.getSteps().get(0);
        Step gym = period.getSteps().get(1);
        Assertions.assertThat(period.getSize()).isEqualTo(105L);
        Assertions.assertThat(period.getSteps()).hasSize(2)
                .contains(task, gym);
        Assertions.assertThat(task.getStart()).isEqualTo(LocalTime.of(9, 00));
        Assertions.assertThat(task.getEnd()).isEqualTo(LocalTime.of(10, 00));
        Assertions.assertThat(gym.getStart()).isEqualTo(LocalTime.of(10, 00));
        Assertions.assertThat(gym.getEnd()).isEqualTo(LocalTime.of(10, 45));
    }

    @Test
    public void whenTryToAddWithReservedTimeThenReturnFalse() {
        Period period = PeriodBuilder.afternoon();
        Assertions.assertThat(period.canAdd(Task.builder()
                .minutes(240L)
                .build())).isFalse();
    }

    @Test
    public void whenAddInAfternoonReturnTrue() {
        Period period = PeriodBuilder.afternoon();
        Assertions.assertThat(period.canAdd(Task.builder()
                .minutes(230L)
                .build())).isTrue();
    }

    @Test
    public void whenHasOnlyGymnasticThenReturnSteps() {
        Period period = PeriodBuilder.afternoon();
        period.applyRequiredSteps();
        Assertions.assertThat(period.getSteps()).hasSize(1);
        Assertions.assertThat(period.getSteps().get(0).getStart()).isEqualTo(LocalTime.of(16, 00));
        Assertions.assertThat(period.getSteps().get(0).getEnd()).isEqualTo(LocalTime.of(16, 10));
    }

    @Test
    public void whenHasEmptyMorningPeriodThenReturnEmptySteps() {
        Period period = PeriodBuilder.morning();
        period.applyRequiredSteps();
        Assertions.assertThat(period.getSteps()).isEmpty();
    }

    @Test
    public void whenHOnlyGymnasticWithLineThenReturnSteps() {
        Period period = PeriodBuilder.afternoon();
        period.add(Task.builder()
                .minutes(230L)
                .name("new task")
                .build());
        period.applyRequiredSteps();
        Assertions.assertThat(period.getSteps()).hasSize(2);
        Assertions.assertThat(period.getSteps().get(1).getStart()).isEqualTo(LocalTime.of(16, 50));
        Assertions.assertThat(period.getSteps().get(1).getEnd()).isEqualTo(LocalTime.of(17, 00));
    }

}