package com.gabn.mutant.service.impl;

import com.gabn.mutant.model.Mutant;
import com.gabn.mutant.model.Stats;
import com.gabn.mutant.repository.MutantRepository;
import com.gabn.mutant.service.MutantService;
import com.gabn.mutant.util.Constants;
import com.gabn.mutant.util.MutantUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MutantServiceImplTest {
    private MutantRepository mutantRepository;
    private MutantService mutantService;
    @MockBean
    private Constants constants;

    @BeforeEach
    void setUp() {
        mutantRepository = mock(MutantRepository.class);
        mutantService = new MutantServiceImpl(mutantRepository);
    }

    @Test
    void validate_dna_mutant_horizontal() {
        String[] mutantDna = {"ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"};

        try (MockedStatic<MutantUtil> mutantUtil = Mockito.mockStatic(MutantUtil.class)) {
            mutantUtil.when(() -> MutantUtil.validateNitrogenBase(any(String[].class))).thenReturn(true);
            mutantUtil.when(() -> MutantUtil.arrangeHorizontalSequence(any(String[].class))).thenReturn(true);
        }
        boolean isMutant = mutantService.validateDNA(mutantDna);
        assertEquals(isMutant, true);
    }

    @Test
    void validate_dna_mutant_vertical() {
        String[] mutantDna = {"ATGCGA","CAGTGC","TTATGT","AGAAGG","CTCCTA","TCACTG"};

        try (MockedStatic<MutantUtil> mutantUtil = Mockito.mockStatic(MutantUtil.class)) {
            mutantUtil.when(() -> MutantUtil.validateNitrogenBase(any(String[].class))).thenReturn(true);
            mutantUtil.when(() -> MutantUtil.arrangeHorizontalSequence(any(String[].class))).thenReturn(false);
            mutantUtil.when(() -> MutantUtil.arrangeVerticalSequence(any(String[].class))).thenReturn(true);
        }
        boolean isMutant = mutantService.validateDNA(mutantDna);
        assertEquals(isMutant, true);
    }

    @Test
    void validate_dna_mutant_diagonal_ppal() {
        String[] mutantDna = {"ATGCCA","CAGTGC","TTATGT","AGAAGG","CTCCTA","TCACTG"};

        try (MockedStatic<MutantUtil> mutantUtil = Mockito.mockStatic(MutantUtil.class)) {
            mutantUtil.when(() -> MutantUtil.validateNitrogenBase(any(String[].class))).thenReturn(true);
            mutantUtil.when(() -> MutantUtil.arrangeHorizontalSequence(any(String[].class))).thenReturn(false);
            mutantUtil.when(() -> MutantUtil.arrangeVerticalSequence(any(String[].class))).thenReturn(false);
            mutantUtil.when(() -> MutantUtil.arrangeDiagonalSequence(any(String[].class))).thenReturn(true);
        }
        boolean isMutant = mutantService.validateDNA(mutantDna);
        assertEquals(isMutant, true);
    }

    @Test
    void validate_dna_mutant_diagonal_sec() {
        String[] mutantDna = {"TTGCCA","CAGTAC","TTAAGT","AGAAGG","CTCCTA","TCACTG"};

        try (MockedStatic<MutantUtil> mutantUtil = Mockito.mockStatic(MutantUtil.class)) {
            mutantUtil.when(() -> MutantUtil.validateNitrogenBase(any(String[].class))).thenReturn(true);
            mutantUtil.when(() -> MutantUtil.arrangeHorizontalSequence(any(String[].class))).thenReturn(false);
            mutantUtil.when(() -> MutantUtil.arrangeVerticalSequence(any(String[].class))).thenReturn(false);
            mutantUtil.when(() -> MutantUtil.arrangeDiagonalSequence(any(String[].class))).thenReturn(true);
        }
        boolean isMutant = mutantService.validateDNA(mutantDna);
        assertEquals(isMutant, true);
    }

    @Test
    void get_stats() {
        Stats stats = new Stats();
        stats.setCountMutantDNA(4);
        stats.setCountHumanDNA(10);
        stats.setRatio(0.4F);
        Stats statsExp;

        String[] mutantDna = {"ATGCCA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"};
        boolean isMutant = true;
        List<Mutant> allMutants = new ArrayList<>();
        allMutants.add(new Mutant(mutantDna, isMutant));
        when(mutantRepository.findAll()).thenReturn(allMutants);
        try (MockedStatic<MutantUtil> mutantUtil = Mockito.mockStatic(MutantUtil.class)) {
            mutantUtil.when(() -> MutantUtil.getStats(any(List.class))).thenReturn(stats);
            statsExp = mutantService.getStats();
        }
        assertEquals(stats, statsExp);
    }

    @Test
    void get_stats_mutants_empty() {
        Stats stats = new Stats();
        stats.setCountMutantDNA(4);
        stats.setCountHumanDNA(10);
        stats.setRatio(0.4F);
        Stats statsExp;

        String[] mutantDna = {"ATGCCA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"};
        boolean isMutant = true;
        List<Mutant> allMutants = new ArrayList<>();
        when(mutantRepository.findAll()).thenReturn(allMutants);
        try (MockedStatic<MutantUtil> mutantUtil = Mockito.mockStatic(MutantUtil.class)) {
            mutantUtil.when(() -> MutantUtil.getStats(any(List.class))).thenReturn(stats);
            statsExp = mutantService.getStats();
        }
        assertEquals(stats, statsExp);
    }
}