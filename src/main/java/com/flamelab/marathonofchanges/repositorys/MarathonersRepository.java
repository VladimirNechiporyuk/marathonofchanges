package com.flamelab.marathonofchanges.repositorys;

import com.flamelab.marathonofchanges.entitys.Marathoner;
import org.bson.types.ObjectId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MarathonersRepository extends MongoRepository<Marathoner, UUID> {

    Optional<Marathoner> findByName(String name);

    List<Marathoner> findByLevel(int level);

}
