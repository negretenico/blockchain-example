package com.example.blockchain.model;

import ch.qos.logback.core.pattern.color.BlackCompositeConverter;
import com.common.functionico.evaluation.Result;
import com.common.functionico.risky.Try;

import java.util.LinkedList;
import java.util.List;

public class BlockChain {
    private final List<Block> chain = new LinkedList<>();
    public BlockChain(Block genesis){
        chain.addFirst(genesis);
    }
    public Result<Block> addBlock(Block block){
        try{
            chain.addLast(block);
            return Result.success(block);
        }catch (Exception e){
            return  Result.failure(e.getMessage());
        }
    }
    public List<Block> getChain(){
        return  chain;
    }
}
