package com.example.blockchain.consensus;

import com.example.blockchain.model.Block;

public sealed interface Consensus  permits ProofOfHistory, ProofOfStake, ProofOfWork{
    Block method(Block block);
}
