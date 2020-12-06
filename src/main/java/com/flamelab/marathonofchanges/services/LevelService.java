package com.flamelab.marathonofchanges.services;

import com.flamelab.marathonofchanges.entitys.Level;

import java.util.List;

public interface LevelService {

    List<Level> createLevels(int amountOfLevels, long stepOfExperienceAdding);

    List<Level> getAllLevels();

    Level getLevelById(int id);

    Level getLevelByExperienceValue(long experienceValue);

    List<Level> updateExperienceValueForAllLevels(long experienceValue);

    int getExperienceStep();

}
