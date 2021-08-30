package com.gabn.mutant.service.impl;

import com.gabn.mutant.model.Mutant;
import com.gabn.mutant.repository.MutantRepository;
import com.gabn.mutant.service.MutantService;
import com.gabn.mutant.model.Stats;
import com.gabn.mutant.util.MutantUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MutantServiceImpl implements MutantService {
    private MutantRepository mutantRepository;

    public MutantServiceImpl() {

    }

    public MutantServiceImpl(MutantRepository mutantRepository) {
        this.mutantRepository = mutantRepository;
    }

    public boolean validateDNA(String[] dna) {
        boolean isMutant = false;

        if (MutantUtil.validateNitrogenBase(dna)) {
            isMutant = MutantUtil.arrangeHorizontalSequence(dna);
            if (!isMutant) {
                isMutant = MutantUtil.arrangeVerticalSequence(dna);
                if (!isMutant) {
                    isMutant = MutantUtil.arrangeDiagonalSequence(dna);
                }
            }
            try {
                saveMutantRecord(dna, isMutant);
            } catch(Exception e) {
                System.out.println("Error saving dna! " + e.getMessage());
            }
        }

        return isMutant;
    }

    public Mutant saveMutantRecord(String[] dna, boolean isMutant) {
        return mutantRepository.save(new Mutant(dna, isMutant));
    }

    public Stats getStats() {
        List<Mutant> allMutants = mutantRepository.findAll();

        return MutantUtil.getStats(allMutants);
    }
}
