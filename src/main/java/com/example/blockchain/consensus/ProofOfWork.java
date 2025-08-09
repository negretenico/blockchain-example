package com.example.blockchain.consensus;

import com.example.blockchain.model.Block;
import com.example.blockchain.service.DifficultyService;
import com.example.blockchain.service.HashService;
import com.example.blockchain.service.TargetService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public final class ProofOfWork implements Consensus {
    private final DifficultyService difficultyService;
    private final TargetService targetService;
    private final HashService hashService;
    public ProofOfWork(DifficultyService difficultyService, TargetService targetService,
                       HashService hashService){
        this.difficultyService=difficultyService;
        this.targetService = targetService;
        this.hashService=hashService;
    }
    @Override
    public Block method(Block block) {
        int diff = difficultyService.getDifficulty();
        String target = targetService.getTarget(diff);

        int nonce = 0;
        String hash = hashService.computeHash(block);

        while (!hash.startsWith(target)) {
            nonce++;
            hash = hashService.computeHash(new Block(block.index(), block.ts(), "", block.previous(), nonce));
        }
        return new Block(block.index(), block.ts(), hash, block.previous(), nonce);
    }
}
