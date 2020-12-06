package com.flamelab.marathonofchanges.entitys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "levels")
@AllArgsConstructor
@NoArgsConstructor
public class Level {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "experience_value")
    private long experienceValue;

    public static Level.Builder builder() {
        return new Level().new Builder();
    }

    @NoArgsConstructor
    public class Builder {

        public Builder id(int id) {
            Level.this.id = id;
            return this;
        }

        public Builder experienceValue(long experienceValue) {
            Level.this.experienceValue = experienceValue;
            return this;
        }

        public Level build() {
            return Level.this;
        }
    }
}
