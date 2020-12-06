package com.flamelab.marathonofchanges.services;

import com.flamelab.marathonofchanges.dtos.MarathonerDto;
import com.flamelab.marathonofchanges.utiles.DataForUpdateMarathonerAfterCompletingTheExercises;

public interface MarathonerPromotingService {

    MarathonerDto updateExperienceAfterCompletingTheExercisesAndTasks(DataForUpdateMarathonerAfterCompletingTheExercises dataAfterTheTask);

}
