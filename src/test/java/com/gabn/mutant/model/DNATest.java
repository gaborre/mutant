package com.gabn.mutant.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DNATest {
    private DNA dnaObject;

    @BeforeEach
    void setUp() {
        dnaObject = new DNA();
    }

    @Test
    void getDna() {
        dnaObject.getDna();
    }

    @Test
    void setDna() {
        String[] mutantDna = {"ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"};
        dnaObject.setDna(mutantDna);
    }
}