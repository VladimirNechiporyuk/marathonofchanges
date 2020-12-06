package com.flamelab.marathonofchanges.repositorys;

import com.flamelab.marathonofchanges.entitys.ExerciseWithTotalQuantity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TotalQuantityOfExerciseRepository extends JpaRepository<ExerciseWithTotalQuantity, Integer> {

    List<ExerciseWithTotalQuantity> findAllByMarathonerId(UUID marathonerId);

    Optional<ExerciseWithTotalQuantity> findByMarathonerIdAndExercise(UUID marathonerId, String exercise);

}
