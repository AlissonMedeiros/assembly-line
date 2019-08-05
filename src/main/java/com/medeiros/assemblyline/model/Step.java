package com.medeiros.assemblyline.model;


import java.time.LocalTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Step {

    private LocalTime start;
    private LocalTime end;
    private String name;
    private Long duration;

}
