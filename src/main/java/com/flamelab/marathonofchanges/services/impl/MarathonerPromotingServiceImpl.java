package com.flamelab.marathonofchanges.services.impl;

import com.flamelab.marathonofchanges.dtos.MarathonerDto;
import com.flamelab.marathonofchanges.dtos.TaskDto;
import com.flamelab.marathonofchanges.entitys.Exercise;
import com.flamelab.marathonofchanges.entitys.ExerciseWithTotalQuantity;
import com.flamelab.marathonofchanges.entitys.Level;
import com.flamelab.marathonofchanges.repositorys.TotalQuantityOfExerciseRepository;
import com.flamelab.marathonofchanges.services.*;
import com.flamelab.marathonofchanges.utiles.DataForUpdateMarathonerAfterCompletingTheExercises;
import com.flamelab.marathonofchanges.utiles.MarathonerExerciseData;
import com.flamelab.marathonofchanges.utiles.MarathonerExerciseDataWithGoal;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.flamelab.marathonofchanges.enums.TaskStatus.DONE;
import static com.flamelab.marathonofchanges.enums.TaskStatus.IN_PROGRESS;

@Service
@Slf4j
public class MarathonerPromotingServiceImpl implements MarathonerPromotingService {

    private ExerciseService exerciseService;
    private LevelService levelService;
    private MarathonersService marathonersService;
    private TasksService tasksService;
    private TotalQuantityOfExerciseRepository totalQuantityOfExerciseRepository;

    @Autowired
    public MarathonerPromotingServiceImpl(ExerciseService exerciseService,
                                          LevelService levelService,
                                          MarathonersService marathonersService,
                                          TasksService tasksService,
                                          TotalQuantityOfExerciseRepository totalQuantityOfExerciseRepository) {
        this.exerciseService = exerciseService;
        this.levelService = levelService;
        this.marathonersService = marathonersService;
        this.tasksService = tasksService;
        this.totalQuantityOfExerciseRepository = totalQuantityOfExerciseRepository;
    }

    @Override
    public MarathonerDto updateExperienceAfterCompletingTheExercisesAndTasks(DataForUpdateMarathonerAfterCompletingTheExercises completedExercisesData) {
        log.info("Updating experience and level for marathoner {}", completedExercisesData.getMarathonerId());
        MarathonerDto marathoner = marathonersService.getMarathonerById(UUID.fromString(completedExercisesData.getMarathonerId()));
        updateExperienceAfterCompletingExercises(marathoner, completedExercisesData.getCompletedExercises());
        updateExperienceAfterCompletingTasks(marathoner, completedExercisesData);
        updateMarathonerLevelIfNeeded(marathoner);
        updateTotalQuantityOfExercise(completedExercisesData);
        return marathoner;
    }

    private void updateExperienceAfterCompletingExercises(MarathonerDto marathoner, List<MarathonerExerciseData> completedExerciseDataList) {
        Map<String, Exercise> completedExercisesByNames = exerciseService.getExercisesListByNames(completedExerciseDataList.stream().map(MarathonerExerciseData::getExerciseName).collect(Collectors.toList()))
                .stream().collect(Collectors.toMap(Exercise::getName, exercise -> exercise));
        long updatedExperience = completedExerciseDataList.stream()
                .mapToLong(completedExercise -> {
                    Exercise exercise = completedExercisesByNames.get(completedExercise.getExerciseName());
                    return exercise.getExperience() * completedExercise.getCompletedQuantity();
                }).sum();
        marathoner.setExperience(marathoner.getExperience() + updatedExperience);
    }

    private void updateExperienceAfterCompletingTasks(MarathonerDto marathoner, DataForUpdateMarathonerAfterCompletingTheExercises data) {
        List<TaskDto> completedTasks = updateTasksAndGetCompletedTasks(marathoner, data.getCompletedExercises());
        for (TaskDto task : completedTasks) {
            marathoner.setExperience(marathoner.getExperience() + task.getExperience());
        }
        marathonersService.updateMarathoner(marathoner);
    }

    private List<TaskDto> updateTasksAndGetCompletedTasks(MarathonerDto marathoner, List<MarathonerExerciseData> completedExerciseData) {
        completedExerciseData.forEach(exerciseData -> updateQuantityInTaskList(marathoner.getId(), exerciseData));
        List<TaskDto> notCompletedTasks = tasksService.getNotCompletedTasksForMarathoner(UUID.fromString(marathoner.getId()));
        return notCompletedTasks.stream()
                .map(this::updateTaskForExercisesAndGetCompletedAfterLastUpdates)
                .collect(Collectors.toList());
    }

    private void updateQuantityInTaskList(String marathonerId, MarathonerExerciseData exerciseData) {
        List<TaskDto> taskList = tasksService.getNotCompletedTasksByMarathonerIdAndExerciseName(UUID.fromString(marathonerId), exerciseData.getExerciseName());
        taskList.forEach(task -> updateQuantityInTask(task, exerciseData));
    }

    private void updateQuantityInTask(TaskDto task, MarathonerExerciseData exerciseData) {
        for (MarathonerExerciseData exerciseDataFromList : task.getMarathonerExerciseDataWithGoalList()) {
            if (exerciseDataFromList.getExerciseName().equals(exerciseData.getExerciseName())) {
                task.setTaskStatus(IN_PROGRESS.name());
                exerciseDataFromList.setCompletedQuantity(exerciseDataFromList.getCompletedQuantity() + exerciseData.getCompletedQuantity());
                break;
            }
        }
        tasksService.updateTask(task);
    }

    private TaskDto updateTaskForExercisesAndGetCompletedAfterLastUpdates(TaskDto task) {
        boolean isTaskDone = false;
        for (MarathonerExerciseDataWithGoal exerciseData : task.getMarathonerExerciseDataWithGoalList()) {
            if (exerciseData.getCompletedQuantity() >= exerciseData.getGoalQuantity()) {
                isTaskDone = true;
            } else {
                isTaskDone = false;
                break;
            }
        }
        if (isTaskDone) {
            task.setTaskStatus(DONE.name());
            tasksService.updateTask(task);
        }
        return task;
    }

    private void updateMarathonerLevelIfNeeded(MarathonerDto marathoner) {
        while (marathonerExperienceBiggerThenLevelExperienceValue(marathoner)) {
            int newLevel = marathoner.getLevel() + 1;
            long updatedExperience = marathoner.getExperience() - levelService.getLevelById(newLevel).getExperienceValue();
            marathoner.setLevel(newLevel);
            marathoner.setExperience(updatedExperience);
            log.info("Marathoner {} up level to {}", marathoner.getId(), newLevel);
            marathonersService.updateMarathoner(marathoner);
        }
    }

    private boolean marathonerExperienceBiggerThenLevelExperienceValue(MarathonerDto marathoner) {
        Level level = levelService.getLevelById(marathoner.getLevel());
        return marathoner.getExperience() >= level.getExperienceValue();
    }

    private void updateTotalQuantityOfExercise(DataForUpdateMarathonerAfterCompletingTheExercises completedExercisesData) {
        List<ExerciseWithTotalQuantity> exercisesForSaving = new ArrayList<>();
        List<MarathonerExerciseData> notExecutedExercises = completedExercisesData.getCompletedExercises();
        Map<String, ExerciseWithTotalQuantity> marathonerExercises = totalQuantityOfExerciseRepository.findAllByMarathonerId(UUID.fromString(completedExercisesData.getMarathonerId()))
                .stream()
                .parallel()
                .collect(Collectors.toMap(ExerciseWithTotalQuantity::getExercise, exercise -> exercise));
        for (MarathonerExerciseData completedExercise : completedExercisesData.getCompletedExercises()) {
            notExecutedExercises.remove(completedExercise);
            ExerciseWithTotalQuantity executedExerciseRecord = marathonerExercises.get(completedExercise.getExerciseName());
            executedExerciseRecord.setQuantity(executedExerciseRecord.getQuantity() + completedExercise.getCompletedQuantity());
            executedExerciseRecord.setLastUpdate(LocalDateTime.now());
            exercisesForSaving.add(executedExerciseRecord);
        }

        for (MarathonerExerciseData completedExercise : notExecutedExercises) {
            exercisesForSaving.add(ExerciseWithTotalQuantity.builder()
                    .marathonerId(UUID.fromString(completedExercisesData.getMarathonerId()))
                    .exercise(completedExercise.getExerciseName())
                    .quantity(completedExercise.getCompletedQuantity())
                    .lastUpdate(LocalDateTime.now())
                    .build());
        }
        totalQuantityOfExerciseRepository.saveAll(exercisesForSaving);
    }

}
