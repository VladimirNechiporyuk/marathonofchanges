package com.flamelab.marathonofchanges.entitys;

import com.flamelab.marathonofchanges.enums.IterationType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "exercises")
@NoArgsConstructor
public class Exercise {

    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "experience")
    private long experience;

    @Column(name = "iteration_type")
    private IterationType iterationType;

    public static Builder builder() {
        return new Exercise().new Builder();
    }

    @NoArgsConstructor
    public class Builder {

        public Builder id(long id) {
            Exercise.this.id = id;
            return this;
        }

        public Builder name(String name) {
            Exercise.this.name = name;
            return this;
        }

        public Builder experience(long experience) {
            Exercise.this.experience = experience;
            return this;
        }

        public Builder iterationType(IterationType iterationType) {
            Exercise.this.iterationType = iterationType;
            return this;
        }

        public Exercise build() {
            return Exercise.this;
        }
    }

}
