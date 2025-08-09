package com.example.blockchain.service;

import com.common.functionico.evaluation.Result;
import com.common.functionico.validation.Valid;
import com.common.functionico.validation.Validated;
import com.example.blockchain.model.Block;
import com.example.blockchain.model.BlockChain;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Gatherers;

import static java.util.stream.Gatherers.windowFixed;

@Service
public class BlockChainValidationService {
    private final HashService hashService;

    public BlockChainValidationService(HashService hashService) {
        this.hashService = hashService;
    }

    public boolean validate(BlockChain blockChain){
        return blockChain.getChain().stream()
                .gather(windowFixed(2))
                .allMatch(pair -> {
                    if(pair.size()<2){
                        return true;
                    }
                    Block previous = pair.get(0);
                    Block current = pair.get(1);
                    return current.hash().equals(hashService.computeHash(current)) &&
                            current.previous().equals(previous.hash());
                });
    }
}
