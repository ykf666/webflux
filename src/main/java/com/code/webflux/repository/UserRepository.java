package com.code.webflux.repository;

import com.code.webflux.domain.User;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Created by yankefei on 2018/12/2.
 */
@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String> {

    Flux<User> findByAgeBetween(int start, int end);

    @Query("{'age':{'$gte':60}}")
    Flux<User> findOldUser();
}
