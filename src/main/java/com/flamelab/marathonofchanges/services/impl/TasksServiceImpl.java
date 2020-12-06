package com.flamelab.marathonofchanges.services.impl;

import com.flamelab.marathonofchanges.dtos.TaskDto;
import com.flamelab.marathonofchanges.dtos.create.CreateTaskDto;
import com.flamelab.marathonofchanges.entitys.Task;
import com.flamelab.marathonofchanges.enums.TaskStatus;
import com.flamelab.marathonofchanges.enums.TaskType;
import com.flamelab.marathonofchanges.exceptions.MarathonerAlreadyExistsException;
import com.flamelab.marathonofchanges.exceptions.TaskNotFoundException;
import com.flamelab.marathonofchanges.repositorys.TasksRepository;
import com.flamelab.marathonofchanges.services.MarathonersService;
import com.flamelab.marathonofchanges.services.TasksService;
import com.flamelab.marathonofchanges.utiles.MarathonerExerciseData;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.flamelab.marathonofchanges.enums.TaskStatus.*;

@Service
@Slf4j
public class TasksServiceImpl implements TasksService {

    private TasksRepository tasksRepository;
    private MarathonersService marathonersService;

    @Autowired
    public TasksServiceImpl(TasksRepository tasksRepository,
                            MarathonersService marathonersService) {
        this.tasksRepository = tasksRepository;
        this.marathonersService = marathonersService;
    }

    @Override
    public TaskDto createTask(CreateTaskDto createTaskDto) {
        validationOnExistingTask(createTaskDto.getTaskName(), UUID.fromString(createTaskDto.getMarathonerId()));
        createTaskDto.getMarathonerExerciseDataWithGoalList().forEach(exercise -> exercise.setCompletedQuantity(0));
        Task task = Task.builder()
                .id(ObjectId.get())
                .name(createTaskDto.getTaskName())
                .experience(createTaskDto.getTaskExperience())
                .marathonerId(createTaskDto.getMarathonerId())
                .taskStatus(NEW)
                .taskType(TaskType.valueOf(createTaskDto.getTaskType()))
                .exercisesAndQuantityList(createTaskDto.getMarathonerExerciseDataWithGoalList())
                .createdDate(LocalDateTime.now())
                .build();
        addNewTaskToMarathoner(task, UUID.fromString(createTaskDto.getMarathonerId()));
        log.info("Creating task {}", task.toString());
        return tasksRepository.save(task).toDto();
    }

    @Override
    public List<TaskDto> getAllTasks() {
        List<Task> tasks = tasksRepository.findAll();
        if (tasks.isEmpty()) {
            log.error("There is no tasks at all");
            throw new TaskNotFoundException("There is no tasks at all");
        } else {
            return tasks.stream()
                    .map(Task::toDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public TaskDto getTaskById(UUID id) {
        Optional<Task> optionalTask = tasksRepository.findById(id);
        if (optionalTask.isPresent()) {
            return optionalTask.get().toDto();
        } else {
            log.error("Task with id {} does not exist", id);
            throw new TaskNotFoundException(String.format("Task with id %s does not exist", id));
        }
    }

    @Override
    public TaskDto getTaskByName(String name, UUID marathonerId) {
        Optional<Task> optionalTask = tasksRepository.findByNameAndMarathonerId(name, marathonerId);
        if (optionalTask.isPresent()) {
            return optionalTask.get().toDto();
        } else {
            log.error("Task with id {} does not exist", name);
            throw new TaskNotFoundException(String.format("Task with id %s does not exist", name));
        }
    }

    @Override
    public List<TaskDto> getAllTaskByMarathonerId(UUID marathonerId) {
        List<Task> tasks = tasksRepository.findAllByMarathonerId(marathonerId);
        if (!tasks.isEmpty()) {
            return tasks.stream()
                    .map(Task::toDto)
                    .collect(Collectors.toList());
        } else {
            log.error("There is no tasks for marathonerId {}", marathonerId);
            throw new TaskNotFoundException(String.format("There is no tasks for marathonerId %s", marathonerId));
        }
    }

    @Override
    public List<TaskDto> getNotCompletedTasksForMarathoner(UUID marathonerId) {
        List<Task> tasks = tasksRepository.findAllByMarathonerIdAndTaskStatusIn(marathonerId, Arrays.asList(NEW, IN_PROGRESS));
        if (!tasks.isEmpty()) {
            return tasks.stream()
                    .map(Task::toDto)
                    .collect(Collectors.toList());
        } else {
            log.error("There is no not completed tasks for marathonerId {}", marathonerId);
            throw new TaskNotFoundException(String.format("There is no not completed tasks for marathonerId %s", marathonerId));
        }
    }

    @Override
     public List<TaskDto> getTasksByMarathonerIdAndTaskStatus(UUID marathonerId, TaskStatus taskStatus) {
        List<Task> tasks = tasksRepository.findAllByMarathonerIdAndTaskStatus(marathonerId, taskStatus);
        if (tasks.isEmpty()) {
            log.warn("There is no tasks for marathoner {} with status {}", marathonerId, taskStatus);
            throw new TaskNotFoundException(String.format("There is no tasks for marathoner %s with status %s", marathonerId, taskStatus));
        }
        return tasks.stream()
                .map(Task::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDto> getNotCompletedTasksByMarathonerIdAndExerciseName(UUID marathonerId, String exerciseName) {
        return getNotCompletedTasksForMarathoner(marathonerId).stream()
                .filter(task -> findAllTasksByExerciseName(task, exerciseName).isPresent())
                .collect(Collectors.toList());
    }

    @Override
    public TaskDto updateTask(TaskDto taskWithUpdates) {
        // TODO: 02.12.2020 если какие-то поля имеют null - не изменять их
        TaskDto oldTaskRecord = getTaskById(UUID.fromString(taskWithUpdates.getId()));
        if (!oldTaskRecord.getName().equals(taskWithUpdates.getName())) {
            validationOnExistingTask(taskWithUpdates.getName(), UUID.fromString(taskWithUpdates.getMarathonerId()));
        }
        Task task = taskWithUpdates.toEntity();
        task.setId(new ObjectId(taskWithUpdates.getId()));
        return tasksRepository.save(task).toDto();
    }

    @Override
    public boolean deleteTask(UUID id) {
        TaskDto task = getTaskById(id);
        tasksRepository.deleteById(id);
        log.info("Task with id {} with status {} was deleted", id, task.getTaskStatus());
        return true;
    }

    private void validationOnExistingTask(String name, UUID marathonerId) {
        Optional<Task> optionalTask = tasksRepository.findByNameAndMarathonerId(name, marathonerId);
        if (optionalTask.isPresent()) {
            log.info("Task with name {} already exists in current ", name);
            throw new MarathonerAlreadyExistsException(String.format("Task with name %s already exists", name));
        } else {
                log.info("Task with name {} already exists in current ", name);
                throw new MarathonerAlreadyExistsException(String.format("Task with name %s already exists", name));
        }
    }

    private void addNewTaskToMarathoner(Task task, UUID marathonerId) {
        marathonersService.addTaskToMarathoner(marathonerId, task.toDto());
    }

    private Optional<TaskDto> findAllTasksByExerciseName(TaskDto task, String exerciseName) {
        for (MarathonerExerciseData exerciseData : task.getMarathonerExerciseDataWithGoalList()) {
            if (exerciseData.getExerciseName().equals(exerciseName)) {
                return Optional.of(task);
            }
        }
        return Optional.empty();
    }
}
