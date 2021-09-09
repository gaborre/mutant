package com.gabn.mutant.service;

import com.gabn.mutant.model.Mutant;
import com.gabn.mutant.model.Stats;
import reactor.core.publisher.Mono;

public interface MutantService {
    boolean validateDNA(String[] dna);
    Mono<Mutant> saveMutantRecord(String[] dna, boolean isMutant) throws Exception;
    Mono<Stats> getStats();
}
