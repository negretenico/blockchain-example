package com.example.blockchain.consensus;

import com.common.functionico.either.Either;
import com.common.functionico.evaluation.Result;
import com.common.functionico.validation.Valid;
import com.example.blockchain.model.Block;
import com.example.blockchain.model.ProofOfStakeValidationStatus;
import com.example.blockchain.service.HashService;
import com.example.blockchain.service.ValidationService;
import org.springframework.stereotype.Service;

@Service
public final class ProofOfStake implements Consensus {
    private final ValidationService validationService;
    private final HashService hashService;
    public ProofOfStake(ValidationService validationService, HashService hashService) {
        this.validationService = validationService;
        this.hashService = hashService;
    }

    @Override
    public Block method(Block block) {
        ProofOfStakeValidationStatus stakeValidationStatus =
                validationService.validate(block);
        if(stakeValidationStatus!=ProofOfStakeValidationStatus.PASS){
            return null;
        }
        String hash = hashService.computeHash(block);
        return block.withComputedHash(hash);
    }
}
