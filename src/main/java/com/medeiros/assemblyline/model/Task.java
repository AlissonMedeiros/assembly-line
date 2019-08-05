package com.medeiros.assemblyline.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Task {

    private String name;
    private Long minutes;

}
