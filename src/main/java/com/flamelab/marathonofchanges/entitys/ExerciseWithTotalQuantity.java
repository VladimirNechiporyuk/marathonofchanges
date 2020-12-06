package com.flamelab.marathonofchanges.entitys;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "exercises_with_total_quantity")
public class ExerciseWithTotalQuantity {

    @Id
    @Column(name = "marathoner_id")
    private UUID marathonerId;

    @Column(name = "exercise")
    private String exercise;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "last_update")
    private LocalDateTime lastUpdate;

    public static ExerciseWithTotalQuantity.Builder builder() {
        return new ExerciseWithTotalQuantity().new Builder();
    }

    @NoArgsConstructor
    public class Builder {

        public Builder marathonerId(UUID marathonerId) {
            ExerciseWithTotalQuantity.this.marathonerId = marathonerId;
            return this;
        }

        public Builder exercise(String exercise) {
            ExerciseWithTotalQuantity.this.exercise = exercise;
            return this;
        }

        public Builder quantity(int quantity){
            ExerciseWithTotalQuantity.this.quantity = quantity;
            return this;
        }

        public Builder lastUpdate(LocalDateTime lastUpdate) {
            ExerciseWithTotalQuantity.this.lastUpdate = lastUpdate;
            return this;
        }

        public ExerciseWithTotalQuantity build() {
            return ExerciseWithTotalQuantity.this;
        }

    }
}
