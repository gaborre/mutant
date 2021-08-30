package com.gabn.mutant.service;

import com.gabn.mutant.model.Mutant;
import com.gabn.mutant.model.Stats;

public interface MutantService {
    boolean validateDNA(String[] dna);
    Mutant saveMutantRecord(String[] dna, boolean isMutant);
    Stats getStats();
}
