package com.example.blockchain.service;

import com.example.blockchain.model.Block;
import com.example.blockchain.model.ProofOfStakeValidationStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ValidationService {
    private final String TAMPER_INDICATOR ="TAMPER";
    private final int STAKE = 20;
    public ProofOfStakeValidationStatus validate(Block block){
        return Optional.ofNullable(block)
                .map(b->{
                    if(b.hash().contains(TAMPER_INDICATOR)){
                        return ProofOfStakeValidationStatus.TAMPERED;
                    }
                    int chance = (int)(Math.random() * 100);
                    return chance < STAKE? ProofOfStakeValidationStatus.FAIL:
                            ProofOfStakeValidationStatus.PASS;
                })
                .orElse(ProofOfStakeValidationStatus.FAIL);
    }
}
