package com.flamelab.marathonofchanges.dtos.create;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateSettingDto {
    private String name;
    private String value;
}
