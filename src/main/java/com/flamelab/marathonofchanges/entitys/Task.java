package com.flamelab.marathonofchanges.entitys;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flamelab.marathonofchanges.dtos.TaskDto;
import com.flamelab.marathonofchanges.enums.TaskStatus;
import com.flamelab.marathonofchanges.enums.TaskType;
import com.flamelab.marathonofchanges.utiles.MarathonerExerciseDataWithGoal;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document("tasks")
public class Task {

    @Id
    @JsonProperty("id")
    private ObjectId id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("given_experience")
    private long experience;

    @JsonProperty("executor")
    private ObjectId marathonerId;

    @JsonProperty("status")
    private TaskStatus taskStatus;

    @JsonProperty("type")
    private TaskType taskType;

    @JsonProperty("creationDate")
    private LocalDateTime createdDate;

    @JsonProperty("lastUpdateDate")
    private LocalDateTime lastUpdateDate;

    @JsonProperty("exercisesList")
    private List<MarathonerExerciseDataWithGoal> marathonerExerciseDataWithGoalList;

    public TaskDto toDto() {
        return TaskDto.builder()
                .id(this.id)
                .name(this.name)
                .experience(this.experience)
                .marathonerId(this.marathonerId)
                .taskStatus(this.taskStatus)
                .taskType(this.taskType)
                .createdDate(this.createdDate)
                .exercisesAndQuantityList(this.marathonerExerciseDataWithGoalList)
                .build();
    }

    public Task toEntity() {
        return Task.builder()
                .id(this.id)
                .name(this.name)
                .experience(this.experience)
                .marathonerId(this.marathonerId.toString())
                .taskStatus(this.taskStatus)
                .taskType(this.taskType)
                .createdDate(this.createdDate)
                .exercisesAndQuantityList(this.marathonerExerciseDataWithGoalList)
                .build();
    }

    public static Task.Builder builder() {
        return new Task().new Builder();
    }

    @NoArgsConstructor
    public class Builder {

        public Builder id(ObjectId id) {
            Task.this.id = id;
            return this;
        }

        public Builder name(String name) {
            Task.this.name = name;
            return this;
        }

        public Builder experience(long experience) {
            Task.this.experience = experience;
            return this;
        }

        public Builder marathonerId(String marathonerId) {
            Task.this.marathonerId = new ObjectId(marathonerId);
            return this;
        }

        public Builder taskStatus(TaskStatus taskStatus) {
            Task.this.taskStatus = taskStatus;
            return this;
        }

        public Builder taskType(TaskType taskType) {
            Task.this.taskType = taskType;
            return this;
        }

        public Builder exercisesAndQuantityList(List<MarathonerExerciseDataWithGoal> marathonerExerciseDataWithGoalList) {
            Task.this.marathonerExerciseDataWithGoalList = marathonerExerciseDataWithGoalList;
            return this;
        }

        public Builder createdDate(LocalDateTime date) {
            Task.this.createdDate = date;
            return this;
        }

        public Task build() {
            return Task.this;
        }
    }

}
