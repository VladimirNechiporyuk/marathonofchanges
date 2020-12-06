package com.flamelab.marathonofchanges.controllers;

import com.flamelab.marathonofchanges.dtos.create.CreateSettingDto;
import com.flamelab.marathonofchanges.entitys.Setting;
import com.flamelab.marathonofchanges.utiles.SettingsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/settings")
public class SettingsController {

    private SettingsUtils settingsUtils;

    @Autowired
    public SettingsController(SettingsUtils settingsUtils) {
        this.settingsUtils = settingsUtils;
    }

    @PostMapping
    public ResponseEntity<Setting> createSetting(@RequestBody CreateSettingDto createSettingDto) {
        return ResponseEntity.ok(settingsUtils.createSetting(createSettingDto));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Setting>> getAllSettings() {
        return ResponseEntity.ok(settingsUtils.getAllSettings());
    }

    @GetMapping("/name")
    public ResponseEntity<Setting> getSettingByName(@RequestParam String name) {
        return ResponseEntity.ok(settingsUtils.getSettingByName(name));
    }

    @PutMapping
    public ResponseEntity<Setting> updateSetting(@RequestParam String name,
                                                 @RequestBody CreateSettingDto updateSettingDto) {
        return ResponseEntity.ok(settingsUtils.updateSetting(name, updateSettingDto));
    }

    @DeleteMapping
    public ResponseEntity<Boolean> deleteSetting(@RequestParam int id) {
        return ResponseEntity.ok(settingsUtils.deleteSetting(id));
    }

}
