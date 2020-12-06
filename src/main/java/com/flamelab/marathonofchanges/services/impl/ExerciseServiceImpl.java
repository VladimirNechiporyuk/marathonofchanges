package com.flamelab.marathonofchanges.services.impl;

import com.flamelab.marathonofchanges.entitys.Exercise;
import com.flamelab.marathonofchanges.enums.IterationType;
import com.flamelab.marathonofchanges.exceptions.ExerciseAlreadyExistsException;
import com.flamelab.marathonofchanges.exceptions.ExerciseNotFoundException;
import com.flamelab.marathonofchanges.repositorys.ExerciseRepository;
import com.flamelab.marathonofchanges.services.ExerciseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ExerciseServiceImpl implements ExerciseService {

    private ExerciseRepository exerciseRepository;

    @Autowired
    public ExerciseServiceImpl(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    @Override
    public Exercise createExercise(String name, long experience, IterationType iterationType) {
        validationOnExistingExercise(name);
        long latestNumber = getLatestExerciseNumber();
        Exercise exercise = Exercise.builder()
                .id(latestNumber + 1)
                .name(name)
                .experience(experience)
                .iterationType(iterationType)
                .build();
        log.info("Creating exercise {}", exercise.toString());
        return exerciseRepository.save(exercise);
    }

    @Override
    public List<Exercise> getAllExercises() {
        List<Exercise> exercises = exerciseRepository.findAll();
        if (exercises.isEmpty()) {
            log.warn("There is no existing exercises");
            throw new ExerciseNotFoundException("There is no existing exercises");
        } else {
            return exercises;
        }
    }

    @Override
    public Exercise getById(int id) {
        Optional<Exercise> optionalExercise = exerciseRepository.findById(id);
        if (optionalExercise.isPresent()) {
            return optionalExercise.get();
        } else {
            log.warn("Exercise with id {} does not exists", id);
            throw new ExerciseNotFoundException(String.format("Exercise with name %s does not exists", id));
        }
    }

    @Override
    public Exercise getByName(String name) {
        Optional<Exercise> optionalExercise = exerciseRepository.findByName(name);
        if (optionalExercise.isPresent()) {
            return optionalExercise.get();
        } else {
            log.warn("Exercise with name {} does not exists", name);
            throw new ExerciseNotFoundException(String.format("Exercise with name %s does not exists", name));
        }
    }

    @Override
    public List<Exercise> getExercisesListByNames(List<String> names) {
        List<Exercise> exercises = exerciseRepository.findAllByNameIn(names);
        if (!exercises.isEmpty()) {
            return exercises;
        } else {
            log.warn("Exercises with names {} does not exists", names);
            throw new ExerciseNotFoundException(String.format("Exercises with names %s does not exists", names));
        }
    }

    @Override
    public List<Exercise> getAllByExperience(long experience) {
        List<Exercise> exercises = exerciseRepository.findByExperience(experience);
        if (!exercises.isEmpty()) {
            return exercises;
        } else {
            log.warn("There is no exercises with experience {}", experience);
            throw new ExerciseNotFoundException(String.format("There is no exercises with experience %s", experience));
        }
    }

    @Override
    public List<Exercise> getAllByIterationType(String iterationType) {
        List<Exercise> exercises = exerciseRepository.findByIterationType(iterationType);
        if (exercises.isEmpty()) {
            return exercises;
        } else {
            log.warn("There is no exercises with exerciseType {}", iterationType);
            throw new ExerciseNotFoundException(String.format("There is no exercises with exerciseType %s", iterationType));
        }
    }

    @Override
    public Exercise updateExercise(int id, Exercise exerciseWithUpdates) {
        // TODO: 02.12.2020 если в любом поле null, не изменять его
        getById(id);
        return Exercise.builder()
                .id(id)
                .name(exerciseWithUpdates.getName())
                .experience(exerciseWithUpdates.getExperience())
                .iterationType(exerciseWithUpdates.getIterationType())
                .build();
    }

    @Override
    public Exercise updateExperienceForExercise(int id, long experience) {
        Exercise exercise = getById(id);
        exercise.setExperience(experience);
        return exerciseRepository.save(exercise);
    }

    @Override
    public boolean deleteExercise(int id) {
        getById(id);
        exerciseRepository.deleteById(id);
        log.info("Exercise with id {} was deleted", id);
        return true;
    }

    private void validationOnExistingExercise(String name) {
        Optional<Exercise> optionalExercise = exerciseRepository.findByName(name);
        if (optionalExercise.isPresent()) {
            log.warn("Exercise with name {} already existing", name);
            throw new ExerciseAlreadyExistsException(String.format("Exercise with name %s already existing", name));
        }
    }

    private long getLatestExerciseNumber() {
        return exerciseRepository.count();
    }
}
