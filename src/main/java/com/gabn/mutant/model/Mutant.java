package com.gabn.mutant.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

public class Mutant {
    @Id
    private String id;
    @Indexed(unique = true)
    private String[] dna;
    private boolean mutant;

    public Mutant() {
    }

    public Mutant(String[] dna, boolean mutant) {
        this.dna = dna;
        this.mutant = mutant;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String[] getDna() {
        return dna;
    }

    public void setDna(String[] dna) {
        this.dna = dna;
    }

    public boolean isMutant() {
        return mutant;
    }

    public void setMutant(boolean mutant) {
        this.mutant = mutant;
    }
}
