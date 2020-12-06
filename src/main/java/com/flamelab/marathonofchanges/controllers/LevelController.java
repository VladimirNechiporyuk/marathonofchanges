package com.flamelab.marathonofchanges.controllers;

import com.flamelab.marathonofchanges.entitys.Level;
import com.flamelab.marathonofchanges.services.LevelService;
import com.flamelab.marathonofchanges.utiles.SettingNames;
import com.flamelab.marathonofchanges.utiles.SettingsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/levels")
public class LevelController {

    private LevelService levelService;
    private SettingsUtils settingsUtils;
    private SettingNames settingNames;

    @Autowired
    public LevelController(LevelService levelService,
                           SettingsUtils settingsUtils,
                           SettingNames settingNames) {
        this.levelService = levelService;
        this.settingsUtils = settingsUtils;
        this.settingNames = settingNames;
    }

    @PostMapping
    public ResponseEntity<List<Level>> createLevel(@RequestParam("amountOfLevels") int amountOfLevels) {
        long stepOfExperienceAdding = Long.valueOf(settingsUtils.getSettingByName(settingNames.experienceStep).getValue());
        return ResponseEntity.ok(levelService.createLevels(amountOfLevels, stepOfExperienceAdding));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Level>> getAllLevels() {
        return ResponseEntity.ok(levelService.getAllLevels());
    }

    @GetMapping("/id")
    public ResponseEntity<Level> getLevelById(@RequestParam("id") int id) {
        return ResponseEntity.ok(levelService.getLevelById(id));
    }

    @GetMapping("/experienceValue")
    public ResponseEntity<Level> getLevelByExperienceValue(@RequestParam("experienceValue") long experienceValue) {
        return ResponseEntity.ok(levelService.getLevelByExperienceValue(experienceValue));
    }

    @PutMapping("/updateExperienceValue")
    public ResponseEntity<List<Level>> updateExperienceValueForLevel(@RequestParam("experienceValue") long experienceValue) {
        return ResponseEntity.ok(levelService.updateExperienceValueForAllLevels(experienceValue));
    }

}
