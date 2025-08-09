package com.example.blockchain.config;

import com.example.blockchain.consensus.ProofOfStake;
import com.example.blockchain.consensus.ProofOfWork;
import com.example.blockchain.model.Block;
import com.example.blockchain.model.BlockChain;
import com.example.blockchain.service.BlockChainValidationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class ConsensusRunner {
    @Bean
    @Profile("proofOfStake")
    public CommandLineRunner runProofOfStake(
            ProofOfStake proofOfStake,
            BlockChainValidationService validationService
    ) {
        return args -> {
            // 1️⃣ Create genesis block (no mining, just initial block)
            Block genesis = new Block(0, System.currentTimeMillis(), "", "0", 0);
            // Assume PoS “mining” is just validating & accepting blocks
            Block validatedGenesis = proofOfStake.method(genesis);
            if (validatedGenesis == null) {
                System.out.println("Failed to validate genesis block");
                return;
            }

            // 2️⃣ Create blockchain with the validated genesis block
            BlockChain blockchain = new BlockChain(validatedGenesis);

            // 3️⃣ "Mine" and add blocks using PoS consensus
            for (int i = 1; i <= 3; i++) {
                Block newBlock = new Block(i, System.currentTimeMillis(), "", blockchain.getChain().getLast().hash(), 0);
                Block validatedBlock = proofOfStake.method(newBlock);
                if (validatedBlock == null) {
                    System.out.println("Failed to validate block " + i);
                    continue;  // skip or retry logic here
                }
                blockchain.addBlock(validatedBlock);
            }

            // 4️⃣ Validate the full blockchain
            boolean isValid = validationService.validate(blockchain);
            System.out.println("Blockchain valid? " + isValid);

            // 5️⃣ Print the chain
            blockchain.getChain().forEach(System.out::println);
        };
    }

    @Bean
    @Profile("proofOfWork")
    public CommandLineRunner run(
            ProofOfWork proofOfWork,
            BlockChainValidationService validationService
    ) {
        return args -> {
            // 1️⃣ Create the genesis block
            Block genesis = new Block(0, System.currentTimeMillis(), "", "0", 0);

            // 2️⃣ Create the blockchain with the genesis block
            BlockChain blockchain =
                    new BlockChain(			proofOfWork.method(genesis));
            // 3️⃣ Mine and add a few more blocks
            for (int i = 1; i <= 3; i++) {
                Block newBlock = new Block(i, System.currentTimeMillis(), "", blockchain.getChain().getLast().hash(), 0);
                blockchain.addBlock(				proofOfWork.method(newBlock));
            }

            // 4️⃣ Validate the blockchain
            boolean isValid = validationService.validate(blockchain);
            System.out.println("Blockchain valid? " + isValid);

            // 5️⃣ Print the chain
            blockchain.getChain().forEach(System.out::println);
        };
    }
}
