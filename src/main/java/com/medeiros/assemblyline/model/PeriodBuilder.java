package com.medeiros.assemblyline.model;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class PeriodBuilder {

    public static final long FOUR_HOURS = 240L;
    public static final long MAX_SIZE = 180L;

    private PeriodBuilder() {

    }

    public static Period afternoon() {
        return Period.builder()
                .start(LocalTime.of(13, 00))
                .end(LocalTime.of(17, 00))
                .maxSize(FOUR_HOURS)
                .size(0L)
                .steps(new ArrayList<>())
                .requiredSteps(Arrays.asList(RequiredStep.LABOR_GYMNASTIC))
                .build();
    }

    public static Period morning() {
        return Period.builder()
                .start(LocalTime.of(9, 00))
                .end(LocalTime.of(12, 00))
                .maxSize(MAX_SIZE)
                .size(0L)
                .steps(new ArrayList<>())
                .requiredSteps(Collections.emptyList())
                .build();
    }

}
