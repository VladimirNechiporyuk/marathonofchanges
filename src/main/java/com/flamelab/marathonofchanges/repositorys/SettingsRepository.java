package com.flamelab.marathonofchanges.repositorys;

import com.flamelab.marathonofchanges.entitys.Setting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SettingsRepository extends JpaRepository<Setting, Integer> {
    Optional<Setting> findByName(String name);
}
