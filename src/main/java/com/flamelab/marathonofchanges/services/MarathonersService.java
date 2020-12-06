package com.flamelab.marathonofchanges.services;

import com.flamelab.marathonofchanges.dtos.MarathonerDto;
import com.flamelab.marathonofchanges.dtos.TaskDto;
import com.flamelab.marathonofchanges.entitys.ExerciseWithTotalQuantity;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.UUID;

public interface MarathonersService {

    MarathonerDto createMarathoner(String marathonerName);

    List<MarathonerDto> getAllMarathoners();

    MarathonerDto getMarathonerById(UUID id);

    MarathonerDto getMarathonerByName(String name);

    List<MarathonerDto> getMarathonersByLevel(int level);

    ExerciseWithTotalQuantity getTotalQuantityByExerciseForMarathoner(UUID marathonerId, String exerciseName);

    List<ExerciseWithTotalQuantity> getAllExercisesWithTotalQuantityForMarathoner(UUID marathonerId);

    MarathonerDto updateMarathoner(MarathonerDto marathoner);

    void addTaskToMarathoner(UUID marathonerId, TaskDto task);

    boolean deleteMarathoner(UUID id);

    void validationOnExistingMarathoner(String name);
}
