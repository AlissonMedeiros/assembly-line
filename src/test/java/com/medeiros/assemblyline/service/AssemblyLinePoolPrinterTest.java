package com.medeiros.assemblyline.service;

import com.medeiros.assemblyline.model.AssemblyLinePool;
import com.medeiros.assemblyline.model.Task;
import java.util.Arrays;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class AssemblyLinePoolPrinterTest {

    private LineBalancer lineBalancer = new LineBalancer();
    private AssemblyLinePoolPrinter printer = new AssemblyLinePoolPrinter();

    @Test
    public void whenHasOneLineThenPrint() {
        AssemblyLinePool pool = lineBalancer.balance(1, Arrays.asList(
                Task.builder().name("1").minutes(100L).build()
                , Task.builder().name("2").minutes(70L).build()
                , Task.builder().name("3").minutes(100L).build()
                , Task.builder().name("4").minutes(100L).build()
                , Task.builder().name("5").minutes(30L).build()
        ));
        List<String> output = printer.print(pool);
        Assertions.assertThat(output).hasSize(9)
                .contains("Assembly line 1 :",
                        "09:00 1 100min",
                        "10:40 2 70min",
                        "12:00 Lunch",
                        "13:00 3 100min",
                        "14:40 4 100min",
                        "16:20 5 30min",
                        "16:50 Ginástica laboral 10min");
    }

    @Test
    public void whenHasTwoLinesThenPrint() {
        AssemblyLinePool pool = lineBalancer.balance(2, Arrays.asList(
                Task.builder().name("1").minutes(100L).build()
                , Task.builder().name("2").minutes(70L).build()
        ));
        List<String> output = printer.print(pool);
        Assertions.assertThat(output).hasSize(10)
                .contains("Assembly line 1 :",
                        "09:00 1 100min",
                        "12:00 Lunch",
                        "16:00 Ginástica laboral 10min",
                        "\n",
                        "Assembly line 2 :",
                        "09:00 2 70min",
                        "12:00 Lunch",
                        "16:00 Ginástica laboral 10min",
                        "\n"
                );
    }

}