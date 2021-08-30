package com.gabn.mutant.util;

import com.gabn.mutant.model.Mutant;
import com.gabn.mutant.model.Stats;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MutantUtil {

    public static boolean validateNitrogenBase(String[] dna) {
        boolean validNB = true;
        boolean validLength = true;

        if (null == dna) {
            return false;
        } else {
            int dnaLength = dna.length;

            for (String dna1 : dna) {
                if (dna1.length() != dnaLength) {
                    validLength = false;
                    break;
                }
                if (!validateCharacters(dna1)) {
                    validNB = false;
                    break;
                }
            }
            return (validLength && validNB);
        }
    }

    public static boolean validateCharacters(String sequence) {
        return sequence.matches(Constants.NB_REGEX);
    }

    public static boolean validateDNA(String nbSequence) {
        return (nbSequence.contains(Constants.NB1)
                || nbSequence.contains(Constants.NB2)
                || nbSequence.contains(Constants.NB3)
                || nbSequence.contains(Constants.NB4));
    }

    public static boolean arrangeHorizontalSequence(String[] dna) {
        boolean isMutant = false;
        for (String dna1 : dna) {
            if (validateDNA(dna1)) {
                isMutant = true;
                break;
            }
        }

        return isMutant;
    }

    public static boolean arrangeVerticalSequence(String[] dna) {
        boolean isMutant = false;
        String[] vDNA = new String[dna.length];
        int i = 0;

        while (i < dna.length) {
            vDNA[i] = "";
            for (String dna1 : dna) {
                vDNA[i] = vDNA[i] + dna1.substring(i, i+1);
            }
            if (validateDNA(vDNA[i])) {
                isMutant = true;
                break;
            }
            i = i + 1;
        }

        return isMutant;
    }

    public static boolean arrangeDiagonalSequence(String[] dna) {
        boolean isMutant = false;
        List<String> dDNA = new ArrayList<>();
        int i = 0;
        int j = 0;
        int k = 0;
        int dnaLength = dna.length;
        int rows = dnaLength - Constants.NB_LENGTH;
        int cols = 0;
        int r = (2 * dnaLength) - 1 - Constants.UNAVAILABLE_SEQUENCE_COUNT;
        StringBuilder sequence = new StringBuilder();

        for (k = 0; k < r; k++) {
            sequence.setLength(0);

            if (rows == 0) {
                for (j = cols; j < dnaLength; j++) {
                    sequence.append(dna[rows].substring(j, j+1));
                    rows = rows + 1;
                }
                rows = 0;
                cols = cols + 1;
            } else {
                for (i = rows; i < dnaLength; i++) {
                    sequence.append(dna[i].substring(cols, cols+1));
                    cols = cols + 1;
                }
                rows = rows - 1;
                cols = 0;
            }
            if (validateDNA(sequence.toString())) {
                isMutant = true;
                break;
            }
            dDNA.add(sequence.toString());
        }
        rows = 3;
        cols = 0;

        if (!isMutant) {
            for (k = 0; k < r; k++) {
                sequence.setLength(0);

                if (rows == (dnaLength - 1)) {
                    for (j = cols; j < dnaLength; j++) {
                        sequence.append(dna[rows].substring(j, j+1));
                        rows = rows - 1;
                    }
                    rows = dnaLength - 1;
                    cols = cols + 1;
                } else {
                    for (i = rows; i >= 0; i--) {
                        sequence.append(dna[i].substring(cols, cols+1));
                        cols = cols + 1;
                    }
                    rows = rows + 1;
                    cols = 0;
                }
                if (validateDNA(sequence.toString())) {
                    isMutant = true;
                    break;
                }
                dDNA.add(sequence.toString());
            }
        }

        return isMutant;
    }

    public static Stats getStats(List<Mutant> allMutants) {
        Stats stats = new Stats();
        if (allMutants.isEmpty()) {
            stats.setCountMutantDNA(0);
            stats.setCountHumanDNA(0);
            stats.setRatio(0);
        } else {
            List<Mutant> mutants = allMutants.stream().filter(mutant -> mutant.isMutant()).collect(Collectors.toList());
            List<Mutant> humans = allMutants.stream().filter(mutant -> !mutant.isMutant()).collect(Collectors.toList());
            int mutantCount = mutants.size();
            int humanCount = humans.size();
            stats.setCountMutantDNA(mutantCount);
            stats.setCountHumanDNA(humanCount);
            stats.setRatio(0);
            if (humans.size() > 0) {
                float result = mutantCount / humanCount;
                float ratio = (float) (Math.round(result * 100.0) / 100.0);
                stats.setRatio(ratio);
            }
        }

        return stats;
    }
}
