package com.medeiros.assemblyline.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;

@Data
public class AssemblyLine {

    public static final String LUNCH = " Lunch";
    private Period morning;
    private Period afternoon;

    public AssemblyLine open() {
        morning = PeriodBuilder.morning();
        afternoon = PeriodBuilder.afternoon();
        return this;
    }

    public void close() {
        morning.applyRequiredSteps();
        afternoon.applyRequiredSteps();
    }

    public void add(Task task) {
        if (morning.canAdd(task)) {
            morning.add(task);
        } else if (afternoon.canAdd(task)) {
            afternoon.add(task);
        } else {
            throw new RuntimeException("The pool was full, try to add more lines! " + task);
        }
    }

    public List<String> print() {
        List<String> output = new ArrayList<>();
        output.addAll(morning.getSteps().stream()
                .map(AssemblyLine::printLine)
                .collect(Collectors.toList()));
        output.add(morning.getEnd() + LUNCH);
        output.addAll(afternoon.getSteps().stream()
                .map(AssemblyLine::printLine)
                .collect(Collectors.toList()));
        return output;
    }

    private static String printLine(Step step) {
        return step.getStart() + " " + step.getName() + " " + step.getDuration() + "min";
    }

}
