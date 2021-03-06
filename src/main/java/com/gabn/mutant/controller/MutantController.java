package com.gabn.mutant.controller;

import com.gabn.mutant.model.DNA;
import com.gabn.mutant.repository.MutantRepository;
import com.gabn.mutant.service.MutantService;
import com.gabn.mutant.model.Stats;
import com.gabn.mutant.service.impl.MutantServiceImpl;
import com.gabn.mutant.util.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@CrossOrigin("*")
@RestController()
public class MutantController {
    private MutantService mutantService;

    public MutantController(MutantRepository mutantRepository) {
        mutantService = new MutantServiceImpl(mutantRepository);
    }

    @PostMapping(path = Constants.MUTANT_URI, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity> validateDNA(@RequestBody(required = false) DNA dna) {
        boolean isMutant = false;
        if (null != dna) {
            isMutant = mutantService.validateDNA(dna.getDna());
        }
        HttpStatus httpStatus = isMutant ? HttpStatus.OK : HttpStatus.FORBIDDEN;

        return Mono.just(new ResponseEntity(null, httpStatus));
    }

    @GetMapping(path = Constants.STATS_URI, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Stats> getStats() {

        return mutantService.getStats();
    }
}
