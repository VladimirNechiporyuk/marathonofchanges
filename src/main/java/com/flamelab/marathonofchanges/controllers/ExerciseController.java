package com.flamelab.marathonofchanges.controllers;

import com.flamelab.marathonofchanges.dtos.create.CreateExerciseDto;
import com.flamelab.marathonofchanges.entitys.Exercise;
import com.flamelab.marathonofchanges.enums.IterationType;
import com.flamelab.marathonofchanges.services.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/exercises")
public class ExerciseController {

    private ExerciseService exerciseService;

    @Autowired
    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @PostMapping
    public ResponseEntity<Exercise> createExercise(@RequestBody CreateExerciseDto createExerciseDto) {
        return ResponseEntity.ok(
                exerciseService.createExercise(
                        createExerciseDto.getName(),
                        createExerciseDto.getExperience(),
                        createExerciseDto.getIterationType()));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Exercise>> getAllExercises() {
        return ResponseEntity.ok(exerciseService.getAllExercises());
    }

    @GetMapping("/id")
    public ResponseEntity<Exercise> getExerciseById(@RequestParam("id") int id) {
        return ResponseEntity.ok(exerciseService.getById(id));
    }

    @GetMapping("/name")
    public ResponseEntity<Exercise> getExerciseByName(@RequestParam("name") String name) {
        return ResponseEntity.ok(exerciseService.getByName(name));
    }

    @GetMapping("/experience")
    public ResponseEntity<List<Exercise>> findByExperience(@RequestParam("experience") long experience) {
        return ResponseEntity.ok(exerciseService.getAllByExperience(experience));
    }

    @GetMapping("/iterationType")
    public ResponseEntity<List<Exercise>> findByExerciseType(@RequestParam("iterationType") String iterationType) {
        return ResponseEntity.ok(exerciseService.getAllByIterationType(iterationType));
    }

    @GetMapping("/info/iterationTypes")
    public ResponseEntity<List<IterationType>> getAllIterationTypes() {
        return ResponseEntity.ok(Arrays.asList(IterationType.values()));
    }

    @PutMapping
    public ResponseEntity<Exercise> updateExercise(@RequestParam("id") int id,
                                                   @RequestBody Exercise exercise) {
        // TODO: 02.12.2020 убрать возможность менять id
        return ResponseEntity.ok(exerciseService.updateExercise(id, exercise));
    }

    @PutMapping("/updateExperience")
    public ResponseEntity<Exercise> updateExperienceForExercise(@RequestParam("id") int id,
                                                                @RequestParam("experience") Integer experience) {
        return ResponseEntity.ok(exerciseService.updateExperienceForExercise(id, experience));
    }

    @DeleteMapping
    public ResponseEntity<Boolean> deleteExercise(@RequestParam("id") int id) {
        return ResponseEntity.ok(exerciseService.deleteExercise(id));
    }

}
