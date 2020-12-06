package com.flamelab.marathonofchanges.repositorys;

import com.flamelab.marathonofchanges.entitys.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExerciseRepository extends JpaRepository<Exercise, Integer> {

    Optional<Exercise> findByName(String name);

    List<Exercise> findAllByNameIn(List<String> names);

    List<Exercise> findByExperience(long experience);

    List<Exercise> findByIterationType(String iterationType);
}
