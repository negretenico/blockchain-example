package com.example.blockchain.service;

import org.springframework.stereotype.Service;

@Service
public class TargetService {
    /**
     * return the target for the
     * {@link com.example.blockchain.consensus.ProofOfWork}
     * for miners to compare against
     *
     * @param dif - difficulty
     * @return left `0` padded string
     */
    public String getTarget(int dif){
        return "0".repeat(dif);
    }
}
