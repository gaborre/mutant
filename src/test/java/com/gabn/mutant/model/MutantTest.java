package com.gabn.mutant.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MutantTest {
    private Mutant mutant1;
    private Mutant mutant2;
    private String[] dna = {"ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"};

    @BeforeEach
    void setUp() {
        mutant1 = new Mutant();
        mutant2 = new Mutant(dna, true);
    }

    @Test
    void getId() {
        mutant1.getId();
    }

    @Test
    void setId() {
        mutant1.setId("1");
    }

    @Test
    void getDna() {
        mutant2.getDna();
    }

    @Test
    void setDna() {
        mutant1.setDna(dna);
    }

    @Test
    void isMutant() {
        mutant2.isMutant();
    }

    @Test
    void setMutant() {
        mutant1.setMutant(true);
    }
}