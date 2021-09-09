package com.gabn.mutant.util;

import com.gabn.mutant.model.Mutant;
import com.gabn.mutant.model.Stats;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class MutantUtilTest {
    private String[] dna;

    @BeforeEach
    void setUp() {
    }

    @Test
    void validate_nitrogen_base_null() {
        dna = null;
        boolean valid = MutantUtil.validateNitrogenBase(dna);
        assertEquals(false, valid);
    }

    @Test
    void validate_nitrogen_base() {
        dna = new String[]{"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        boolean valid = MutantUtil.validateNitrogenBase(dna);
        assertEquals(true, valid);
    }

    @Test
    void validate_nitrogen_base_invalid_length() {
        dna = new String[]{"AGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        boolean valid = MutantUtil.validateNitrogenBase(dna);
        assertEquals(false, valid);
    }

    @Test
    void validate_nitrogen_base_invalid_character() {
        dna = new String[]{"ATGCZA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        boolean valid = MutantUtil.validateNitrogenBase(dna);
        assertEquals(false, valid);
    }

    @Test
    void validate_characters_true() {
        boolean valid = MutantUtil.validateCharacters("ACCCCT");
        assertEquals(true, valid);
    }

    @Test
    void validate_characters_false() {
        boolean valid = MutantUtil.validateCharacters("ACZCCT");
        assertEquals(false, valid);
    }

    @Test
    void validate_dna() {
        boolean valid = MutantUtil.validateDNA("ACCCCT");
        assertEquals(true, valid);
    }

    @Test
    void arrange_horizontal_sequence() {
        dna = new String[]{"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        boolean valid = MutantUtil.arrangeHorizontalSequence(dna);
        assertEquals(true, valid);
    }

    @Test
    void arrange_vertical_sequence() {
        dna = new String[]{"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        boolean valid = MutantUtil.arrangeVerticalSequence(dna);
        assertEquals(true, valid);
    }

    @Test
    void arrange_diagonal_sequence() {
        dna = new String[]{"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        boolean valid = MutantUtil.arrangeDiagonalSequence(dna);
        assertEquals(true, valid);
    }

    @Test
    void arrange_secundary_diagonal_sequence() {
        dna = new String[]{"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        boolean valid = MutantUtil.arrangeSecundaryDiagonalSequence(dna);
        assertEquals(false, valid);
    }

    @Test
    void get_stats() {
        String[] mutantDna = {"ATGCCA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"};
        boolean isMutant = true;
        Flux<Mutant> allMutants2 = Flux.just(new Mutant(mutantDna, isMutant));
        Mono<Stats> expMonoStats = MutantUtil.getStats(allMutants2);

        assertNotNull(expMonoStats);
    }
}