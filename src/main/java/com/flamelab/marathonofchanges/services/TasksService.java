package com.flamelab.marathonofchanges.services;

import com.flamelab.marathonofchanges.dtos.TaskDto;
import com.flamelab.marathonofchanges.dtos.create.CreateTaskDto;
import com.flamelab.marathonofchanges.enums.TaskStatus;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.UUID;

public interface TasksService {

    TaskDto createTask(CreateTaskDto createTaskDto);

    List<TaskDto> getAllTasks();

    TaskDto getTaskById(UUID taskId);

    TaskDto getTaskByName(String name, UUID marathonerId);

    List<TaskDto> getAllTaskByMarathonerId(UUID marathonerId);

    List<TaskDto> getNotCompletedTasksForMarathoner(UUID marathonerId);

    List<TaskDto> getTasksByMarathonerIdAndTaskStatus(UUID marathonerId, TaskStatus taskStatus);

    List<TaskDto> getNotCompletedTasksByMarathonerIdAndExerciseName(UUID marathonerId, String taskName);

    TaskDto updateTask(TaskDto task);

    boolean deleteTask(UUID taskId);
}
