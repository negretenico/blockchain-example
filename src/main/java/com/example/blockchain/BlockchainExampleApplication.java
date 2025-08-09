package com.example.blockchain;

import com.example.blockchain.consensus.ProofOfWork;
import com.example.blockchain.model.Block;
import com.example.blockchain.model.BlockChain;
import com.example.blockchain.service.BlockChainValidationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BlockchainExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlockchainExampleApplication.class, args);
	}
}
