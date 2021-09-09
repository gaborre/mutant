package com.gabn.mutant.controller;

import com.gabn.mutant.model.DNA;
import com.gabn.mutant.model.Mutant;
import com.gabn.mutant.model.Stats;
import com.gabn.mutant.repository.MutantRepository;
import com.gabn.mutant.service.MutantService;
import com.gabn.mutant.util.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebFluxTest
class MutantControllerTest {
    @Autowired
    private WebTestClient webTestClient;
    @Mock
    private MutantService mutantService;
    @MockBean
    private MutantRepository mutantRepository;
    private JacksonTester<DNA> mutantDNA;
    private DNA mutantDnaRequest200;
    private DNA mutantDnaRequest403;
    private Stats statsResponse;
    private Mono<Stats> monoStats;
    private Flux<Mutant> allMutants;

    @BeforeEach
    void setUp() {
        mutantDnaRequest200 = new DNA();
        mutantDnaRequest403 = new DNA();
        String[] dna200 = {"ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"};
        String[] dna403 = {"TTGCAA","CAGTGC","TTATGT","AGAAGG","CACCTA","TCACTG"};
        mutantDnaRequest200.setDna(dna200);
        mutantDnaRequest403.setDna(dna403);
        statsResponse = new Stats();
        statsResponse.setCountMutantDNA(4);
        statsResponse.setCountHumanDNA(10);
        statsResponse.setRatio(0.4F);
        monoStats = Mono.just(statsResponse);

        Mutant mutant1 = new Mutant(dna200, true);
        Mutant mutant2 = new Mutant(dna403, false);
        allMutants = Flux.just(mutant1, mutant2);
    }

    @Test
    void validate_dna_200_ok() throws Exception {

        when(mutantService.validateDNA(any())).thenReturn(true);

        webTestClient.post().uri("/mutant")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(mutantDnaRequest200), DNA.class)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void validate_dna_403_forbidden() throws Exception {
        when(mutantService.validateDNA(any())).thenReturn(false);

        webTestClient.post().uri(Constants.MUTANT_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(mutantDnaRequest403), DNA.class)
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    void get_stats_ok() throws Exception {

        when(mutantService.getStats()).thenReturn(monoStats);
        when(mutantRepository.findAll()).thenReturn(allMutants);

        webTestClient.get().uri(Constants.STATS_URI)

                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
    }
}