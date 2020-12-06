package com.flamelab.marathonofchanges.services.impl;

import com.flamelab.marathonofchanges.dtos.MarathonerDto;
import com.flamelab.marathonofchanges.dtos.TaskDto;
import com.flamelab.marathonofchanges.entitys.ExerciseWithTotalQuantity;
import com.flamelab.marathonofchanges.entitys.Marathoner;
import com.flamelab.marathonofchanges.exceptions.ExerciseNotFoundException;
import com.flamelab.marathonofchanges.exceptions.MarathonerAlreadyExistsException;
import com.flamelab.marathonofchanges.exceptions.MarathonerNotFoundException;
import com.flamelab.marathonofchanges.repositorys.MarathonersRepository;
import com.flamelab.marathonofchanges.repositorys.TotalQuantityOfExerciseRepository;
import com.flamelab.marathonofchanges.services.MarathonersService;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MarathonersServiceImpl implements MarathonersService {

    private MarathonersRepository marathonersRepository;
    private TotalQuantityOfExerciseRepository totalQuantityOfExerciseRepository;

    @Autowired
    public MarathonersServiceImpl(MarathonersRepository marathonersRepository,
                                  TotalQuantityOfExerciseRepository totalQuantityOfExerciseRepository) {
        this.marathonersRepository = marathonersRepository;
        this.totalQuantityOfExerciseRepository = totalQuantityOfExerciseRepository;
    }

    @Override
    public MarathonerDto createMarathoner(String name) {
        validationOnExistingMarathoner(name);
        Marathoner marathoner = Marathoner.builder()
                .id(UUID.randomUUID())
                .name(name)
                .level(0)
                .experience(0)
                .tasks(new ArrayList<>())
                .creationDate(LocalDateTime.now())
                .build();
        log.info("Creating marathoner {}", marathoner.toString());
        return marathonersRepository.save(marathoner).toDto();
    }

    @Override
    public List<MarathonerDto> getAllMarathoners() {
        List<Marathoner> marathoners = marathonersRepository.findAll();
        if (marathoners.isEmpty()) {
            log.warn("There is no existing marathoners");
            throw new MarathonerNotFoundException("There is no existing marathoners");
        } else {
            return marathoners.stream()
                    .map(Marathoner::toDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public MarathonerDto getMarathonerById(UUID id) {
        Optional<Marathoner> marathoner = marathonersRepository.findById(id);
        if (marathoner.isPresent()) {
            return marathoner.get().toDto();
        } else {
            log.warn("Marathoner with id {} does not exists", id);
            throw new MarathonerNotFoundException(String.format("Marathoner with id %s does not exists", id));
        }
    }

    @Override
    public MarathonerDto getMarathonerByName(String name) {
        Optional<Marathoner> marathoner = marathonersRepository.findByName(name);
        if (marathoner.isPresent()) {
            return marathoner.get().toDto();
        } else {
            log.warn("Marathoner with id {} does not exist", name);
            throw new MarathonerNotFoundException(String.format("Marathoner with id %s does not exists", name));
        }
    }

    @Override
    public List<MarathonerDto> getMarathonersByLevel(int level) {
        List<Marathoner> marathoners = marathonersRepository.findByLevel(level);
        if (marathoners.isEmpty()) {
            log.warn("There is no marathoners with level {}", level);
            throw new MarathonerNotFoundException(String.format("There is no marathoners with level %s", level));
        } else {
            return marathoners.stream()
                    .map(Marathoner::toDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public ExerciseWithTotalQuantity getTotalQuantityByExerciseForMarathoner(UUID marathonerId, String exerciseName) {
        Optional<ExerciseWithTotalQuantity> optionalExercise = totalQuantityOfExerciseRepository.findByMarathonerIdAndExercise(marathonerId, exerciseName);
        if (optionalExercise.isPresent()) {
            return optionalExercise.get();
        } else {
            log.warn("Marathoner with id {} didn't execute exercise with name {}", marathonerId, exerciseName);
            throw new ExerciseNotFoundException(String.format("Marathoner with id %s didn't executing exercise with name %s", marathonerId, exerciseName));
        }
    }

    @Override
    public List<ExerciseWithTotalQuantity> getAllExercisesWithTotalQuantityForMarathoner(UUID marathonerId) {
        List<ExerciseWithTotalQuantity> allExecutedExercises = totalQuantityOfExerciseRepository.findAllByMarathonerId(marathonerId);
        if (allExecutedExercises.isEmpty()) {
            log.warn("Marathoner with id {} didn't execute any exercises");
            throw new ExerciseNotFoundException(String.format("Marathoner with id %s didn't execute any exercises", marathonerId));
        } else {
            return allExecutedExercises;
        }
    }

    @Override
    public MarathonerDto updateMarathoner(MarathonerDto marathoner) {
        // TODO: 02.12.2020 если какой-то параметр null - не изменять его
        marathoner.setLastUpdateDate(LocalDateTime.now());
        return marathonersRepository.save(marathoner.toEntity()).toDto();
    }

    @Override
    public void addTaskToMarathoner(UUID marathonerId, TaskDto task) {
        // TODO: 05.12.2020 апдейтить у марафонера только поле тасков
        Marathoner marathoner = getMarathonerById(marathonerId).toEntity();
        marathoner.getTasks().add(UUID.fromString(task.getId()));
        marathoner.setTasks(marathoner.getTasks());
        marathonersRepository.save(marathoner);
    }

    @Override
    public boolean deleteMarathoner(UUID id) {
        MarathonerDto marathoner = getMarathonerById(id);
        marathonersRepository.delete(marathoner.toEntity());
        log.info("Marathoner with id {} was deleted", id);
        return true;
    }

    @Override
    public void validationOnExistingMarathoner(String name) {
        Optional<Marathoner> optionalMarathoner = marathonersRepository.findByName(name);
        if (optionalMarathoner.isPresent()) {
            log.warn("Marathoner with name {} already exists", name);
            throw new MarathonerAlreadyExistsException(String.format("Marathoner with name %s already exists", name));
        }
    }

}
