package com.example.blockchain.consensus;

import com.example.blockchain.model.Block;
import com.example.blockchain.model.BlockChain;
import com.example.blockchain.model.PublicParameters;
import com.example.blockchain.service.HashService;
import com.example.blockchain.service.PoHVDFService;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public final class ProofOfHistory implements Consensus {
    private final PoHVDFService service;
    private final HashService hashService;
    private final PublicParameters pp;

    public ProofOfHistory(PoHVDFService service, HashService hashService) {
        this.service = service;
        this.hashService = hashService;
        this.pp = service.setup(512, 100);
    }

    @Override
    public Block method(Block block) {
        String computedHash = hashService.computeHash(block);
        BigInteger x = new BigInteger(1, hexToBytes(computedHash));
        BigInteger y = service.eval(x, pp);

        return block.withComputedHash(computedHash).withProof(y.toString(16));
    }

    private byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i+1), 16));
        }
        return data;
    }
    public PublicParameters publicParameters(){
        return this.pp;
    }
}

