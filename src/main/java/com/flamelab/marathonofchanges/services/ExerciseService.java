package com.flamelab.marathonofchanges.services;

import com.flamelab.marathonofchanges.entitys.Exercise;
import com.flamelab.marathonofchanges.enums.IterationType;

import java.util.List;

public interface ExerciseService {

    Exercise createExercise(String name, long experience, IterationType iterationType);

    List<Exercise> getAllExercises();

    Exercise getById(int id);

    Exercise getByName(String name);

    List<Exercise> getExercisesListByNames(List<String> names);

    List<Exercise> getAllByExperience(long experience);

    List<Exercise> getAllByIterationType(String exerciseType);

    Exercise updateExercise(int id, Exercise exercise);

    Exercise updateExperienceForExercise(int id, long experience);

    boolean deleteExercise(int id);
}
