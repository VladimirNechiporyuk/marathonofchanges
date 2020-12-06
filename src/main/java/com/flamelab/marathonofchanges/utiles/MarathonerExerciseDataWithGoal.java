package com.flamelab.marathonofchanges.utiles;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class MarathonerExerciseDataWithGoal extends MarathonerExerciseData {

    private int goalQuantity;

    public MarathonerExerciseDataWithGoal(String exerciseName, int completedQuantity, int goalQuantity) {
        super(exerciseName, completedQuantity);
        this.goalQuantity = goalQuantity;
    }
}
