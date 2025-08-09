package com.example.blockchain.config;

import com.example.blockchain.consensus.ProofOfHistory;
import com.example.blockchain.consensus.ProofOfStake;
import com.example.blockchain.consensus.ProofOfWork;
import com.example.blockchain.model.Block;
import com.example.blockchain.model.BlockChain;
import com.example.blockchain.model.PublicParameters;
import com.example.blockchain.service.BlockChainValidationService;
import com.example.blockchain.service.PoHVDFService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

@Configuration
public class ConsensusRunner {
    private  void handle(BlockChain blockchain,
                         Function<Block,Block> consensusMethod,
                         BlockChainValidationService validationService,
                         Optional<Function<List<Block>,Boolean>> validation){
        for (int i = 1; i <= 3; i++) {
            Block newBlock = new Block(i, System.currentTimeMillis(), "",
                    blockchain.getChain().getLast().hash(), 0,"");
            blockchain.addBlock(consensusMethod.apply(newBlock));
        }
        boolean isValid = validationService.validate(blockchain,validation);
        System.out.println("Blockchain valid? " + isValid);
        blockchain.getChain().forEach(System.out::println);
    }
    @Bean
    @Profile("proofOfHistory")
    public CommandLineRunner runProofOfHistory(
            ProofOfHistory proofOfHistory,
            PoHVDFService poHVDFService,
            BlockChainValidationService blockChainValidationService
    ){
        return args ->{
            // 1️⃣ Create the genesis block
            Block genesis = new Block(0, System.currentTimeMillis(), "", "0",
                    0,"");
            BlockChain chain = new BlockChain(proofOfHistory.method(genesis));
            handle(chain,proofOfHistory::method,blockChainValidationService,
                    Optional.of(pairs->{
                        if(pairs.size()<2){
                            return true;
                        }
                        Block current = pairs.getFirst();
                        return poHVDFService.verify(new BigInteger(current.hash(),16),
                                new BigInteger(current.pohProof(),16),
                                proofOfHistory.publicParameters());
                    }));
        };
    }
    @Bean
    @Profile("proofOfStake")
    public CommandLineRunner runProofOfStake(
            ProofOfStake proofOfStake,
            BlockChainValidationService validationService
    ) {
        return args -> {
            // 1️⃣ Create genesis block (no mining, just initial block)
            Block genesis = new Block(0, System.currentTimeMillis(), "", "0",
                    0,"");
            // Assume PoS “mining” is just validating & accepting blocks
            Block validatedGenesis = proofOfStake.method(genesis);
            if (validatedGenesis == null) {
                System.out.println("Failed to validate genesis block");
                return;
            }
            // 2️⃣ Create blockchain with the validated genesis block
            BlockChain blockchain = new BlockChain(validatedGenesis);
            handle(blockchain,proofOfStake::method, validationService,Optional.empty());
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
            Block genesis = new Block(0, System.currentTimeMillis(), "", "0",
                    0,"");

            // 2️⃣ Create the blockchain with the genesis block
            BlockChain blockchain =
                    new BlockChain(proofOfWork.method(genesis));
            // 3️⃣ Mine and add a few more blocks
            handle(blockchain, proofOfWork::method,
                    validationService,Optional.empty());
        };
    }
}
