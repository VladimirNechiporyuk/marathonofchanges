package com.flamelab.marathonofchanges.controllers;

import com.flamelab.marathonofchanges.dtos.TaskDto;
import com.flamelab.marathonofchanges.dtos.create.CreateTaskDto;
import com.flamelab.marathonofchanges.enums.TaskStatus;
import com.flamelab.marathonofchanges.services.TasksService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TasksController {

    private TasksService tasksService;

    @Autowired
    public TasksController(TasksService tasksService) {
        this.tasksService = tasksService;
    }

    @PostMapping
    public ResponseEntity<TaskDto> createTask(@RequestBody CreateTaskDto createTaskDto) {
        return ResponseEntity.ok(tasksService.createTask(createTaskDto));
    }

    @GetMapping("/all")
    public ResponseEntity<List<TaskDto>> getAllTasks() {
        return ResponseEntity.ok(tasksService.getAllTasks());
    }

    @GetMapping("/id")
    public ResponseEntity<TaskDto> getTaskById(@RequestParam("id") UUID id) {
        return ResponseEntity.ok(tasksService.getTaskById(id));
    }

    @GetMapping("/executor")
    public ResponseEntity<List<TaskDto>> getTaskByExecutor(@RequestParam("marathonerId") UUID marathonerId) {
        return ResponseEntity.ok(tasksService.getAllTaskByMarathonerId(marathonerId));
    }

    @GetMapping("/nameAndExecutor")
    public ResponseEntity<TaskDto> getTaskByName(@RequestParam("name") String name, @RequestParam("marathonerId") UUID marathonerId) {
        return ResponseEntity.ok(tasksService.getTaskByName(name, marathonerId));
    }

    @GetMapping("/statusAndExecutor")
    public ResponseEntity<List<TaskDto>> getTaskByTaskStatusAndMarathonerId(@RequestParam("marathonerId") UUID marathonerId,
                                                                            @RequestParam("taskStatus") TaskStatus taskStatus) {
        return ResponseEntity.ok(tasksService.getTasksByMarathonerIdAndTaskStatus(marathonerId, taskStatus));
    }

    @PutMapping
    public ResponseEntity<TaskDto> updateTask(@RequestBody TaskDto task) {
        // TODO: 02.12.2020 убрать возможность изменять id
        return ResponseEntity.ok(tasksService.updateTask(task));
    }

    @DeleteMapping
    public ResponseEntity<Boolean> deleteTaskById(@RequestParam("id") UUID id) {
        return ResponseEntity.ok(tasksService.deleteTask(id));
    }
}
