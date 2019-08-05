package com.medeiros.assemblyline.model;

import static com.medeiros.assemblyline.model.RequiredStepBuilder.getRequiredStep;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Period {

    private LocalTime start;
    private LocalTime end;
    private Long size = 0L;
    private Long maxSize;
    private List<Step> steps;
    private List<RequiredStep> requiredSteps;

    public boolean canAdd(Task task) {
        return task.getMinutes() <= freeTime();
    }

    public void add(Task task) {
        Step step = Step.builder()
                .name(task.getName())
                .duration(task.getMinutes())
                .start(getStartTime())
                .end(getEndTime(task.getMinutes()))
                .build();
        steps.add(step);
    }

    public List<Step> getSteps() {
        return Collections.unmodifiableList(steps);
    }

    public Long getMaxSize() {
        Long requiredSize = requiredSteps.stream()
                .mapToLong(r -> r.getDuration())
                .sum();
        return maxSize - requiredSize;
    }

    public void applyRequiredSteps() {
        requiredSteps.forEach(requiredStep -> {
            LocalTime suggestedStartTime = getStartTime();
            Step step = getRequiredStep(requiredStep, suggestedStartTime);
            steps.add(step);
        });
    }

    private LocalTime getEndTime(Long minutes) {
        LocalTime endTime = start.plusMinutes(size).plusMinutes(minutes);
        size += minutes;
        return endTime;
    }

    private LocalTime getStartTime() {
        return start.plusMinutes(size);
    }

    private Long freeTime() {
        return getMaxSize() - size;
    }

}
