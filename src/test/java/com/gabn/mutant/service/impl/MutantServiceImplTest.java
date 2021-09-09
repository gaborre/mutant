package com.gabn.mutant.service.impl;

import com.gabn.mutant.model.Mutant;
import com.gabn.mutant.model.Stats;
import com.gabn.mutant.repository.MutantRepository;
import com.gabn.mutant.service.MutantService;
import com.gabn.mutant.util.Constants;
import com.gabn.mutant.util.MutantUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MutantServiceImplTest {
    private MutantRepository mutantRepository;
    private MutantService mutantService;
    @MockBean
    private Constants constants;
    private Stats stats;
    private Mono<Stats> monoStats;
    private Flux<Mutant> allMutants;

    @BeforeEach
    void setUp() {
        mutantRepository = mock(MutantRepository.class);
        mutantService = new MutantServiceImpl(mutantRepository);

        stats = new Stats();
        stats.setCountMutantDNA(4);
        stats.setCountHumanDNA(10);
        stats.setRatio(0.4F);
        monoStats = Mono.just(stats);

        String[] dna200 = {"ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"};
        String[] dna403 = {"TTGCAA","CAGTGC","TTATGT","AGAAGG","CACCTA","TCACTG"};
        Mutant mutant1 = new Mutant(dna200, true);
        Mutant mutant2 = new Mutant(dna403, false);
        allMutants = Flux.just(mutant1, mutant2);
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
            mutantUtil.when(() -> MutantUtil.arrangeDiagonalSequence(any(String[].class))).thenReturn(false);
            mutantUtil.when(() -> MutantUtil.arrangeSecundaryDiagonalSequence(any(String[].class))).thenReturn(true);
        }
        boolean isMutant = mutantService.validateDNA(mutantDna);
        assertEquals(isMutant, true);
    }

    @Test
    void validate_dna_mutant_horizontal_throw_exception() throws Exception {
        String[] mutantDna = {"ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"};

        try (MockedStatic<MutantUtil> mutantUtil = Mockito.mockStatic(MutantUtil.class)) {
            mutantUtil.when(() -> MutantUtil.validateNitrogenBase(any(String[].class))).thenReturn(true);
            mutantUtil.when(() -> MutantUtil.arrangeHorizontalSequence(any(String[].class))).thenReturn(true);
        }
        Assertions.assertThrows(Exception.class, () -> {
            mutantService.saveMutantRecord(eq(mutantDna), eq(true));
        });
        boolean isMutant = mutantService.validateDNA(mutantDna);
        assertEquals(isMutant, true);
    }

    @Test
    void get_stats() {
        Mono<Stats> statsExp;

        String[] mutantDna = {"ATGCCA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"};
        boolean isMutant = true;
        Flux<Mutant> allMutants2 = Flux.just(new Mutant(mutantDna, isMutant));
        when(mutantRepository.findAll()).thenReturn(allMutants2);
        try (MockedStatic<MutantUtil> mutantUtil = Mockito.mockStatic(MutantUtil.class)) {
            mutantUtil.when(() -> MutantUtil.getStats(any(Flux.class))).thenReturn(monoStats);
            statsExp = mutantService.getStats();
        }
        assertEquals(monoStats, statsExp);
    }

    @Test
    void get_stats_mutants_empty() {

        Mono<Stats> statsExp;

        String[] mutantDna = {"ATGCCA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"};
        boolean isMutant = true;
        Flux<Mutant> allMutants2 = Flux.empty();
        when(mutantRepository.findAll()).thenReturn(allMutants2);
        try (MockedStatic<MutantUtil> mutantUtil = Mockito.mockStatic(MutantUtil.class)) {
            mutantUtil.when(() -> MutantUtil.getStats(any(Flux.class))).thenReturn(monoStats);
            statsExp = mutantService.getStats();
        }
        assertEquals(monoStats, statsExp);
    }
}