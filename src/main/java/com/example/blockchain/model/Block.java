package com.example.blockchain.model;

public record Block(int index, long ts, String hash, String previous,
                    int nonce,
                    String pohProof) {
    public String getIdentifier(){
        return String.format("%s.%s.%s.%s",index,previous,ts,nonce);
    }
    public Block withComputedHash(String hash) {
        return new Block(index, ts, hash, previous, nonce,pohProof);
    }

    public Block withNonce(int nonce) {
        return new Block(index, ts, hash, previous, nonce,pohProof);
    }
    public Block withProof(String pohProof){
        return  new Block(index,ts,hash,previous,nonce,pohProof);
    }
}
