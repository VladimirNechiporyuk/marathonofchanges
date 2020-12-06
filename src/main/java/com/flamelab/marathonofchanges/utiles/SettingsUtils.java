package com.flamelab.marathonofchanges.utiles;

import com.flamelab.marathonofchanges.dtos.create.CreateSettingDto;
import com.flamelab.marathonofchanges.entitys.Setting;
import com.flamelab.marathonofchanges.exceptions.SettingDoesNotFoundExistsException;
import com.flamelab.marathonofchanges.repositorys.SettingsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class SettingsUtils {

    private SettingsRepository settingsRepository;

    @Autowired
    public SettingsUtils(SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    public Setting createSetting(CreateSettingDto createSettingDto) {
        Setting setting = new Setting(settingsRepository.count(), createSettingDto.getName(), createSettingDto.getValue());
        return settingsRepository.save(setting);
    }

    public List<Setting> getAllSettings() {
        return settingsRepository.findAll();
    }

    public Setting getSettingByName(String name) {
        Optional<Setting> optionalSetting = settingsRepository.findByName(name);
        if (optionalSetting.isPresent()) {
            return optionalSetting.get();
        } else {
            log.info("Setting with name {} does not exists", name);
            throw new SettingDoesNotFoundExistsException(String.format("Setting with name %s does not exists", name));
        }
    }

    public Setting updateSetting(String name, CreateSettingDto updateSettingDto) {
        Setting setting = getSettingByName(name);
        setting.setName(updateSettingDto.getName());
        setting.setValue(updateSettingDto.getValue());
        return settingsRepository.save(setting);
    }

    public boolean deleteSetting(int id) {
        Optional<Setting> optionalSetting = settingsRepository.findById(id);
        if (optionalSetting.isPresent()) {
            settingsRepository.deleteById(id);
            log.debug("Setting {} was deleted", optionalSetting.get());
            return true;
        } else {
            log.info("Setting with id {} does not exists", id);
            throw new SettingDoesNotFoundExistsException(String.format("Setting with id %d does not exists", id));
        }
    }

}
