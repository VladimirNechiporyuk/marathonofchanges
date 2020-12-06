package com.flamelab.marathonofchanges.repositorys;

import com.flamelab.marathonofchanges.entitys.Task;
import com.flamelab.marathonofchanges.enums.TaskStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TasksRepository extends MongoRepository<Task, UUID> {

    Optional<Task> findByNameAndMarathonerId(String name, UUID marathonerId);

    List<Task> findAllByMarathonerId(UUID marathonerId);

    List<Task> findAllByMarathonerIdAndTaskStatus(UUID id, TaskStatus taskStatus);

    List<Task> findAllByMarathonerIdAndTaskStatusIn(UUID id, List<TaskStatus> taskStatuses);

}
