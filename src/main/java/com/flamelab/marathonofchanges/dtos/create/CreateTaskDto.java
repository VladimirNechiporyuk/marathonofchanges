package com.flamelab.marathonofchanges.dtos.create;

import com.flamelab.marathonofchanges.utiles.MarathonerExerciseDataWithGoal;
import lombok.Data;

import java.util.List;

@Data
public class CreateTaskDto {
    private String taskName;
    private long taskExperience;
    private String marathonerId;
    private String taskType;
    private List<MarathonerExerciseDataWithGoal> marathonerExerciseDataWithGoalList;
}
