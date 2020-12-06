package com.flamelab.marathonofchanges.dtos;

import com.flamelab.marathonofchanges.entitys.Marathoner;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class MarathonerDto {

    private String id;
    private String name;
    private int level;
    private long experience;
    private List<String> tasks;
    private LocalDateTime creationDate;
    private LocalDateTime lastUpdateDate;

    public Marathoner toEntity() {
        return Marathoner.builder()
                .id(UUID.fromString(id))
                .name(this.name)
                .level(this.level)
                .experience(this.experience)
                .tasks(this.tasks)
                .creationDate(this.creationDate)
                .build();
    }

    public static MarathonerDto.Builder builder() {
        return new MarathonerDto().new Builder();
    }

    @NoArgsConstructor
    public class Builder {

        public Builder id(String id) {
            MarathonerDto.this.id = id;
            return this;
        }

        public Builder name(String name) {
            MarathonerDto.this.name = name;
            return this;
        }

        public Builder level(int level) {
            MarathonerDto.this.level = level;
            return this;
        }

        public Builder experience(long experience) {
            MarathonerDto.this.experience = experience;
            return this;
        }

        public Builder tasks(List<String> tasks) {
            MarathonerDto.this.tasks = tasks;
            return this;
        }

        public Builder creationDate(LocalDateTime date) {
            MarathonerDto.this.creationDate = date;
            return this;
        }

        public MarathonerDto build() {
            return MarathonerDto.this;
        }
    }

}
