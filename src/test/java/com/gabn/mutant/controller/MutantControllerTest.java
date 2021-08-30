package com.gabn.mutant.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabn.mutant.model.DNA;
import com.gabn.mutant.model.Stats;
import com.gabn.mutant.repository.MutantRepository;
import com.gabn.mutant.service.MutantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MutantControllerTest {
    private MockMvc mvc;
    @Mock
    private MutantService mutantService;
    @InjectMocks
    private MutantController mutantController;
    @Mock
    private MutantRepository mutantRepository;
    private JacksonTester<DNA> mutantDNA;
    private JacksonTester<Stats> statsTester;
    private DNA mutantDnaRequest200;
    private DNA mutantDnaRequest403;
    private Stats statsResponse;

    @BeforeEach
    void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());
        mutantController = new MutantController(mutantRepository);
        mvc = MockMvcBuilders.standaloneSetup(mutantService, mutantController).build();
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
    }

    @Test
    void validate_dna_200_ok() throws Exception {

        when(mutantService.validateDNA(any())).thenReturn(true);

        mvc.perform(post("/mutant")
                .contentType(MediaType.APPLICATION_JSON)
        .content(mutantDNA.write(mutantDnaRequest200).getJson()))
                .andExpect(status().isOk());
    }

    @Test
    void validate_dna_403_forbidden() throws Exception {

        when(mutantService.validateDNA(any())).thenReturn(false);

        mvc.perform(post("/mutant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mutantDNA.write(mutantDnaRequest403).getJson()))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void get_stats_ok() throws Exception {

        when(mutantService.getStats()).thenReturn(statsResponse);

        MockHttpServletResponse response = mvc.perform(get("/stats")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
    }
}