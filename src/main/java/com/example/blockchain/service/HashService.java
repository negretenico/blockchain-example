package com.example.blockchain.service;

import com.common.functionico.risky.Try;
import com.example.blockchain.model.Block;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
public class HashService {
    public String computeHash(Block block){
        return Try.of(() -> MessageDigest.getInstance("SHA-256"))
                .map(digest -> {
                    String data = block.getIdentifier();

                    byte[] hashBytes = digest.digest(data.getBytes(StandardCharsets.UTF_8));

                    // Convert to hex using streams
                    return IntStream.range(0, hashBytes.length)
                            .mapToObj(i -> String.format("%02x", hashBytes[i]))
                            .collect(Collectors.joining());
                })
                .getOrElse("FAILURE");
    }
}
