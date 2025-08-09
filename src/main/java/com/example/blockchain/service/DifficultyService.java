package com.example.blockchain.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class DifficultyService {
    /**
     * Randomly generate the difficulty for the miners
     * @return int representing the difficult between 1 and 5
     */
    public int getDifficulty(){
        Random random = new Random();
        return random.nextInt(1,5);
    }
}
