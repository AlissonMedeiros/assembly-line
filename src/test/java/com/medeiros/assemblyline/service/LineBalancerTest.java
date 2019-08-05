package com.medeiros.assemblyline.service;

import com.medeiros.assemblyline.model.AssemblyLinePool;
import com.medeiros.assemblyline.model.Step;
import com.medeiros.assemblyline.model.Task;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

public class LineBalancerTest {

    @InjectMocks
    private LineBalancer lineBalancer;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void whenPoolArrayIsNullThenReturnError() {
        Assertions.assertThatThrownBy(() -> lineBalancer.balance(0, null))
                .hasMessage("Pool can't be empty.")
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void whenTasksIsNullThenReturnError() {
        Assertions.assertThatThrownBy(() -> lineBalancer.balance(2, null))
                .hasMessage("Tasks can't be null.")
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    public void whenHasOnePoolThenExecuteSingleBalance() {
        AssemblyLinePool pool = lineBalancer.balance(1, Arrays.asList(Task.builder().name("1").minutes(10L).build()));
        Assertions.assertThat(pool.getPool().get(0).getMorning().getSteps())
                .hasSize(1);
    }

    @Test
    public void whenHasOnePoolWithFullCapacityThenExecuteBalance() {
        AssemblyLinePool pool = lineBalancer.balance(1, Arrays.asList(
                Task.builder().name("1").minutes(100L).build()
                , Task.builder().name("2").minutes(70L).build()
                , Task.builder().name("3").minutes(100L).build()
                , Task.builder().name("4").minutes(100L).build()
                , Task.builder().name("5").minutes(30L).build()
        ));
        Assertions.assertThat(pool.getPool().get(0).getMorning().getSteps()).hasSize(2);
        Assertions.assertThat(pool.getPool().get(0).getAfternoon().getSteps()).hasSize(4);
    }

    @Test
    public void whenHasOnePoolMorningFullCapacityThenExecuteBalance() {
        AssemblyLinePool pool = lineBalancer.balance(1, Arrays.asList(
                Task.builder().name("1").minutes(170L).build()
                , Task.builder().name("2").minutes(210L).build()
        ));
        Assertions.assertThat(pool.getPool().get(0).getMorning().getSteps()).hasSize(1);
        Assertions.assertThat(pool.getPool().get(0).getAfternoon().getSteps()).hasSize(2);
    }

    @Test
    public void whenHasMorningTimeThenExecuteBalance() {
        AssemblyLinePool pool = lineBalancer.balance(1, Arrays.asList(
                Task.builder().name("1").minutes(170L).build()
                , Task.builder().name("2").minutes(210L).build()
                , Task.builder().name("3").minutes(10L).build()
        ));
        List<Step> morningSteps = pool.getPool().get(0).getMorning().getSteps();
        List<Step> afternoonSteps = pool.getPool().get(0).getAfternoon().getSteps();
        Assertions.assertThat(morningSteps).hasSize(2);
        Assertions.assertThat(morningSteps.get(0).getStart()).isEqualTo(LocalTime.of(9, 00));
        Assertions.assertThat(morningSteps.get(0).getEnd()).isEqualTo(LocalTime.of(11, 50));
        Assertions.assertThat(morningSteps.get(1).getStart()).isEqualTo(LocalTime.of(11, 50));
        Assertions.assertThat(morningSteps.get(1).getEnd()).isEqualTo(LocalTime.of(12, 00));
        Assertions.assertThat(afternoonSteps).hasSize(2);
        Assertions.assertThat(afternoonSteps.get(0).getStart()).isEqualTo(LocalTime.of(13, 00));
        Assertions.assertThat(afternoonSteps.get(0).getStart()).isEqualTo(LocalTime.of(13, 00));
        Assertions.assertThat(afternoonSteps.get(1).getStart()).isEqualTo(LocalTime.of(16, 30));
        Assertions.assertThat(afternoonSteps.get(1).getEnd()).isEqualTo(LocalTime.of(16, 40));
    }

    @Test
    public void whenHasTwoPoolThenExecuteBalance() {
        AssemblyLinePool pool = lineBalancer.balance(2, Arrays.asList(
                Task.builder().name("1").minutes(50L).build()
                , Task.builder().name("2").minutes(50L).build()
                , Task.builder().name("3").minutes(50L).build()
                , Task.builder().name("4").minutes(50L).build()
                , Task.builder().name("5").minutes(50L).build()
                , Task.builder().name("6").minutes(50L).build()
                , Task.builder().name("7").minutes(50L).build()
                , Task.builder().name("8").minutes(50L).build()
                , Task.builder().name("9").minutes(50L).build()
                , Task.builder().name("10").minutes(50L).build()
                , Task.builder().name("11").minutes(50L).build()
                , Task.builder().name("12").minutes(50L).build()
        ));
        Assertions.assertThat(pool.getPool().get(0).getMorning().getSteps()).hasSize(3);
        Assertions.assertThat(pool.getPool().get(0).getAfternoon().getSteps()).hasSize(4);
        Assertions.assertThat(pool.getPool().get(1).getMorning().getSteps()).hasSize(3);
        Assertions.assertThat(pool.getPool().get(1).getAfternoon().getSteps()).hasSize(4);
    }

    @Test
    public void whenHasTwoPoolOverThenReturnError() {
        Assertions.assertThatThrownBy(() -> lineBalancer.balance(1, Arrays.asList(
                Task.builder().name("1").minutes(50L).build()
                , Task.builder().name("2").minutes(50L).build()
                , Task.builder().name("3").minutes(50L).build()
                , Task.builder().name("4").minutes(50L).build()
                , Task.builder().name("5").minutes(50L).build()
                , Task.builder().name("6").minutes(50L).build()
                , Task.builder().name("7").minutes(50L).build()
                , Task.builder().name("8").minutes(100L).build()
                , Task.builder().name("9").minutes(150L).build()
        ))).isExactlyInstanceOf(RuntimeException.class)
                .hasMessage("The pool was full, try to add more lines! Task(name=8, minutes=100)");
    }
}
