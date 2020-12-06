package com.flamelab.marathonofchanges;

import com.flamelab.marathonofchanges.repositorys.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories(basePackageClasses = {MarathonersRepository.class, TasksRepository.class})
@EnableJpaRepositories(basePackageClasses = {ExerciseRepository.class, LevelsRepository.class, TotalQuantityOfExerciseRepository.class, SettingsRepository.class})
@SpringBootApplication
public class MarathonofchangesApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarathonofchangesApplication.class, args);
	}

}
