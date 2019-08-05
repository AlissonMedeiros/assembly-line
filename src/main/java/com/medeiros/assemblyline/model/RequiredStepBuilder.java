package com.medeiros.assemblyline.model;

import java.time.LocalTime;

public class RequiredStepBuilder {

    private RequiredStepBuilder() {

    }

    public static Step getRequiredStep(RequiredStep requiredStep, LocalTime suggestedStartTime) {
        LocalTime startTime = geRequiredStartTime(requiredStep, suggestedStartTime);
        LocalTime endTime = getRequiredEndTime(requiredStep, startTime);
        return Step.builder()
                .name(requiredStep.getName())
                .duration(requiredStep.getDuration())
                .start(startTime)
                .end(endTime)
                .build();
    }

    private static LocalTime getRequiredEndTime(RequiredStep requiredStep, LocalTime startTime) {
        return startTime.plusMinutes(requiredStep.getDuration());
    }

    private static LocalTime geRequiredStartTime(RequiredStep requiredStep, LocalTime startTime) {
        if (requiredStep.getStart().compareTo(startTime) > 0) {
            return requiredStep.getStart();
        }
        return startTime;
    }

}
