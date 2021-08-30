package com.gabn.mutant.repository;

import com.gabn.mutant.model.Mutant;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MutantRepository extends MongoRepository<Mutant, String> {
}
