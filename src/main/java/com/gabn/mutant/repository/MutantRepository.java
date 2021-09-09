package com.gabn.mutant.repository;

import com.gabn.mutant.model.Mutant;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MutantRepository extends ReactiveMongoRepository<Mutant, String> {
}
