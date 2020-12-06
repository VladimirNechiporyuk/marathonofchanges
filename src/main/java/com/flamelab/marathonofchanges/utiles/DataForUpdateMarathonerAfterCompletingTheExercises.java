package com.flamelab.marathonofchanges.utiles;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DataForUpdateMarathonerAfterCompletingTheExercises {

    private String marathonerId;
    private List<MarathonerExerciseData> completedExercises;
}
