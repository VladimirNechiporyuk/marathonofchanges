package com.flamelab.marathonofchanges.dtos.create;

import com.flamelab.marathonofchanges.enums.IterationType;
import lombok.Data;

@Data
public class CreateExerciseDto {
    private String name;
    private long experience;
    private IterationType iterationType;
}
