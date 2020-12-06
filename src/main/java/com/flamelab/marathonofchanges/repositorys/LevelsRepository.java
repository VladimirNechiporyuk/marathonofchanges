package com.flamelab.marathonofchanges.repositorys;

import com.flamelab.marathonofchanges.entitys.Level;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LevelsRepository extends JpaRepository<Level, Integer> {

    Optional<Level> findByExperienceValue(long experienceValue);
}
