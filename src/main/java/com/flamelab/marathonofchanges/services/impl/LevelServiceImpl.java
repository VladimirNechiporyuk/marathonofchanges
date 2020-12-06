package com.flamelab.marathonofchanges.services.impl;

import com.flamelab.marathonofchanges.dtos.create.CreateSettingDto;
import com.flamelab.marathonofchanges.entitys.Level;
import com.flamelab.marathonofchanges.exceptions.LevelNotFoundException;
import com.flamelab.marathonofchanges.repositorys.LevelsRepository;
import com.flamelab.marathonofchanges.services.LevelService;
import com.flamelab.marathonofchanges.utiles.SettingNames;
import com.flamelab.marathonofchanges.utiles.SettingsUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LevelServiceImpl implements LevelService {

    private LevelsRepository levelsRepository;
    private SettingsUtils settingsUtils;
    private SettingNames settingNames;

    @Autowired
    public LevelServiceImpl(LevelsRepository levelsRepository,
                            SettingsUtils settingsUtils,
                            SettingNames settingNames) {
        this.levelsRepository = levelsRepository;
        this.settingsUtils = settingsUtils;
        this.settingNames = settingNames;
    }

    @Override
    public List<Level> createLevels(int amountOfLevels, long stepOfExperienceAdding) {
        List<Level> createdLevels = new ArrayList<>();
        Level firstLevel = creatingFirstLevelIfItNotExists();
        if (firstLevel != null) {
            amountOfLevels--;
            createdLevels.add(firstLevel);
        }
        Level latestLevel = levelsRepository.getOne(getLatestLevelNumber().intValue());
        for (int i = 0; i < amountOfLevels; i++) {
            Level levelForSave = Level.builder()
                    .id(latestLevel.getId() + 1)
                    .experienceValue(latestLevel.getExperienceValue() + stepOfExperienceAdding)
                    .build();
            log.info("Creating level {}", levelForSave.toString());
            createdLevels.add(levelForSave);
            latestLevel = levelForSave;
        }
        createdLevels = levelsRepository.saveAll(createdLevels);
        sortingLevelsById();
        return createdLevels;
    }

    @Override
    public List<Level> getAllLevels() {
        List<Level> levels = levelsRepository.findAll();
        if (levels.isEmpty()) {
            log.warn("There is no levels in db");
            throw new LevelNotFoundException("There is no levels in db");
        } else {
            levels.sort(Comparator.comparingInt(Level::getId));
            return levels;
        }
    }

    @Override
    public Level getLevelById(int id) {
        Optional<Level> optionalLevel = levelsRepository.findById(id);
        if (optionalLevel.isPresent()) {
            return optionalLevel.get();
        } else {
            log.warn("Level with id {} does not exists", id);
            throw new LevelNotFoundException(String.format("Level with name %s does not exists", id));
        }
    }

    @Override
    public Level getLevelByExperienceValue(long experienceValue) {
        Optional<Level> optionalLevel = levelsRepository.findByExperienceValue(experienceValue);
        if (optionalLevel.isPresent()) {
            return optionalLevel.get();
        } else {
            log.warn("Level with experienceValue {} does not exists", experienceValue);
            throw new LevelNotFoundException(String.format("Level with experienceValue %s does not exists", experienceValue));
        }
    }

    @Override
    public List<Level> updateExperienceValueForAllLevels(long experienceValue) {
        settingsUtils.updateSetting(settingNames.experienceStep, new CreateSettingDto(settingNames.experienceStep, String.valueOf(experienceValue)));
        List<Level> createdLevels = createLevels(getAllLevels().size(), experienceValue);
        levelsRepository.deleteAll();
        return levelsRepository.saveAll(createdLevels);
    }

    @Override
    public int getExperienceStep() {
        return Integer.valueOf(settingsUtils.getSettingByName(settingNames.experienceStep).getValue());
    }

    private Level creatingFirstLevelIfItNotExists() {
        Level levelForSaving = null;
        if (levelsRepository.count() == 0) {
            log.info("Creating first level");
            levelForSaving = Level.builder()
                    .id(1)
                    .experienceValue(0)
                    .build();
            levelsRepository.save(levelForSaving);
        }
        return levelForSaving;
    }

    private Long getLatestLevelNumber() {
        return levelsRepository.count();
    }

    private void sortingLevelsById() {
        List<Level> sortedLevels = getAllLevels().stream()
                .sorted(Comparator.comparingInt(Level::getId))
                .collect(Collectors.toList());
        levelsRepository.deleteAll();
        levelsRepository.saveAll(sortedLevels);
    }
}
