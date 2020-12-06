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
@Table(name = "settings")
@AllArgsConstructor
@NoArgsConstructor
public class Setting {

    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "value")
    private String value;

    public static Setting.Builder builder() {
        return new Setting().new Builder();
    }

    public class Builder {

        public Builder id(long id) {
            Setting.this.id = id;
            return this;
        }

        public Builder name(String name) {
            Setting.this.name = name;
            return this;
        }

        public Builder value(String value) {
            Setting.this.value = value;
            return this;
        }

        public Setting build() {
            return Setting.this;
        }

    }
}
