package com.flamelab.marathonofchanges.dtos;

import com.flamelab.marathonofchanges.entitys.Task;
import com.flamelab.marathonofchanges.enums.TaskStatus;
import com.flamelab.marathonofchanges.enums.TaskType;
import com.flamelab.marathonofchanges.utiles.MarathonerExerciseDataWithGoal;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TaskDto {
    private String id;
    private String name;
    private long experience;
    private String marathonerId;
    private String taskStatus;
    private String taskType;
    private LocalDateTime createdDate;
    private List<MarathonerExerciseDataWithGoal> marathonerExerciseDataWithGoalList;

    public Task toEntity() {
        return Task.builder()
                .id(new ObjectId(this.id))
                .name(this.name)
                .experience(this.experience)
                .marathonerId(this.marathonerId)
                .taskStatus(TaskStatus.valueOf(this.taskStatus))
                .taskType(TaskType.valueOf(this.taskType))
                .createdDate(this.createdDate)
                .exercisesAndQuantityList(this.marathonerExerciseDataWithGoalList)
                .build();
    }

    public static TaskDto.Builder builder() {
        return new TaskDto().new Builder();
    }

    @NoArgsConstructor
    public class Builder {

        public TaskDto.Builder id(ObjectId id) {
            TaskDto.this.id = id.toHexString();
            return this;
        }

        public TaskDto.Builder name(String name) {
            TaskDto.this.name = name;
            return this;
        }

        public TaskDto.Builder experience(long experience) {
            TaskDto.this.experience = experience;
            return this;
        }

        public TaskDto.Builder marathonerId(ObjectId marathonerId) {
            TaskDto.this.marathonerId = marathonerId.toHexString();
            return this;
        }

        public TaskDto.Builder taskStatus(TaskStatus taskStatus) {
            TaskDto.this.taskStatus = taskStatus.name();
            return this;
        }

        public TaskDto.Builder taskType(TaskType taskType) {
            TaskDto.this.taskType = taskType.name();
            return this;
        }

        public TaskDto.Builder exercisesAndQuantityList(List<MarathonerExerciseDataWithGoal> exercisesAndQuantityList) {
            TaskDto.this.marathonerExerciseDataWithGoalList = exercisesAndQuantityList;
            return this;
        }

        public TaskDto.Builder createdDate(LocalDateTime date) {
            TaskDto.this.createdDate = date;
            return this;
        }

        public TaskDto build() {
            return TaskDto.this;
        }
    }


}
