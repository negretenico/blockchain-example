package com.example.blockchain.service;

import com.example.blockchain.model.PublicParameters;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.stream.IntStream;

@Service
public class PoHVDFService {
    private final SecureRandom rnd = new SecureRandom();

    public PublicParameters setup(int bits, int time){
        BigInteger p = BigInteger.probablePrime(bits,rnd);
        BigInteger q = BigInteger.probablePrime(bits,rnd);
        return  new PublicParameters(p.multiply(q),time);
    }
    public BigInteger eval(BigInteger x, PublicParameters setup){
        BigInteger modulus = setup.modulus();
        BigInteger y = x.mod(modulus);
        for(int i =0; i< setup.time(); i++){
            y = y.multiply(y).mod(modulus);
        }
        return y;
    }
    public boolean verify(BigInteger x, BigInteger y, PublicParameters pp){
        return eval(x,pp).equals(y);
    }
}
