package com.flamelab.marathonofchanges.entitys;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flamelab.marathonofchanges.dtos.MarathonerDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Document("marathoners")
public class Marathoner {

    @Id
    @JsonProperty("id")
    private UUID id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("level")
    private int level;

    @JsonProperty("experience")
    private long experience;

    @JsonProperty("tasks")
    private List<UUID> tasks;

    @JsonProperty("created_Date")
    private LocalDateTime creationDate;

    @JsonProperty("last_update_date")
    private LocalDateTime lastUpdateDate;

    public MarathonerDto toDto() {
        return MarathonerDto.builder()
                .id(id.toString())
                .name(this.name)
                .level(this.level)
                .experience(this.experience)
                .tasks(this.tasks.stream().map(UUID::toString).collect(Collectors.toList()))
                .creationDate(this.creationDate)
                .build();
    }

    public static Marathoner.Builder builder() {
        return new Marathoner().new Builder();
    }

    @NoArgsConstructor
    public class Builder {

        public Builder id(UUID id) {
            Marathoner.this.id = id;
            return this;
        }

        public Builder name(String name) {
            Marathoner.this.name = name;
            return this;
        }

        public Builder level(int level) {
            Marathoner.this.level = level;
            return this;
        }

        public Builder experience(long experience) {
            Marathoner.this.experience = experience;
            return this;
        }

        public Builder tasks(List<String> tasks) {
            Marathoner.this.tasks = tasks.stream().map(UUID::fromString).collect(Collectors.toList());
            return this;
        }

        public Builder creationDate(LocalDateTime date) {
            Marathoner.this.creationDate = date;
            return this;
        }

        public Marathoner build() {
            return Marathoner.this;
        }
    }
}
