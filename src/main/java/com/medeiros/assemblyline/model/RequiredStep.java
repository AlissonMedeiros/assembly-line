package com.medeiros.assemblyline.model;


import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RequiredStep {

    LABOR_GYMNASTIC("Ginástica laboral", LocalTime.of(16, 00, 00)
            , LocalTime.of(17, 00, 00)
            , 10L);

    private String name;
    private LocalTime start;
    private LocalTime end;
    private Long duration;

}
