package com.flamelab.marathonofchanges.controllers;

import com.flamelab.marathonofchanges.dtos.MarathonerDto;
import com.flamelab.marathonofchanges.entitys.ExerciseWithTotalQuantity;
import com.flamelab.marathonofchanges.services.MarathonersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/marathoners")
public class MarathonerAdminController {

    private MarathonersService marathonersService;

    @Autowired
    public MarathonerAdminController(MarathonersService marathonersService) {
        this.marathonersService = marathonersService;
    }

    @PostMapping
    public ResponseEntity<MarathonerDto> createMarathoner(@RequestParam("name") String name) {
        return ResponseEntity.ok(marathonersService.createMarathoner(name));
    }

    @GetMapping("/all")
    public ResponseEntity<List<MarathonerDto>> getAllMarathoners() {
        return ResponseEntity.ok(marathonersService.getAllMarathoners());
    }

    @GetMapping("/id")
    public ResponseEntity<MarathonerDto> getMarathoner(@RequestParam("id") UUID id) {
        return ResponseEntity.ok(marathonersService.getMarathonerById(id));
    }

    @GetMapping("/name")
    public ResponseEntity<MarathonerDto> getMarathonerByName(@RequestParam("name") String name) {
        return ResponseEntity.ok(marathonersService.getMarathonerByName(name));
    }

    @GetMapping("/level")
    public ResponseEntity<List<MarathonerDto>> getMarathonerByLevel(@RequestParam("level") int level) {
        return ResponseEntity.ok(marathonersService.getMarathonersByLevel(level));
    }

    @GetMapping("/totalQuantityOfExercise")
    public ResponseEntity<ExerciseWithTotalQuantity> getTotalQuantityOfExercise(@RequestParam("marathonerId") UUID marathonerId,
                                                                                @RequestParam("exerciseName") String exerciseName) {
        return ResponseEntity.ok(marathonersService.getTotalQuantityByExerciseForMarathoner(marathonerId, exerciseName));
    }

    @GetMapping("/allTotalQuantityOfExercise")
    public ResponseEntity<List<ExerciseWithTotalQuantity>> getAllTotalQuantityOfExercise(@RequestParam("marathonerId") UUID marathonerId) {
        return ResponseEntity.ok(marathonersService.getAllExercisesWithTotalQuantityForMarathoner(marathonerId));
    }

    @PutMapping
    public ResponseEntity<MarathonerDto> updateMarathoner(@RequestBody MarathonerDto marathoner) {
        // TODO: 02.12.2020 убрать возможность изменять список тасков при апдейте, а так же убрать возможность менять id
        marathonersService.validationOnExistingMarathoner(marathoner.getName());
        return ResponseEntity.ok(marathonersService.updateMarathoner(marathoner));
    }

    @DeleteMapping
    public ResponseEntity<Boolean> deleteMarathoner(@RequestParam("id") UUID id) {
        return ResponseEntity.ok(marathonersService.deleteMarathoner(id));
    }
}
