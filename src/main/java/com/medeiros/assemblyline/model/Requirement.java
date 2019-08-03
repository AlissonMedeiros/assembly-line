package com.medeiros.assemblyline.model;


import java.time.LocalTime;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Requirement {

    LABOR_GYMNASTIC(LocalTime.of(16, 00, 00)
            , LocalTime.of(17, 00, 00)
            , 60L
            , 999999);

    private LocalTime start;
    private LocalTime end;
    private Long duration;
    private int order;

}
