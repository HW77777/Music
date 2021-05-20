package com.example.music;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;

public class Block {
    public String hash;
    public String previousHash;
    public String merkleRoot;
    //public ArrayList<Transaction> transactions = new ArrayList<Transaction>();
    public CopyOnWriteArrayList<Song> mp3Infos=new CopyOnWriteArrayList<>();
    public long timeStamp;
    public int nonce;
    public int height;
    public String address;
    public String name;

    public Block(String previousHash) {
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash();
        //this.address=node;
    }

    public String calculateHash() {
        String calculatedhash = StringUtil.applySha256(
                previousHash +
                        Long.toString(timeStamp) +
                        Integer.toString(nonce) +
                        merkleRoot
        );
        return calculatedhash;
    }

    //增加随机数完成计算难题
    public void mineBlock(int difficulty) {
        merkleRoot = StringUtil.getMerkleRoot(mp3Infos);//transactions
        String target = StringUtil.getDificultyString(difficulty);
        while(!hash.substring( 0, difficulty).equals(target)) {
            nonce ++;
            hash = calculateHash();
        }
        System.out.println("Block Mined!!! : " + Thread.currentThread().getName());//hash
    }

    /*public boolean addTransaction(Transaction transaction) {
        //处理交易并检查是否有效，创世纪区块将被忽略
        if(transaction == null) return false;
        if((previousHash != "0")) {
            if((transaction.processTransaction() != true)) {
                System.out.println("Transaction failed to process. Discarded.");
                return false;
            }
        }

        transactions.add(transaction);
        System.out.println("Transaction Successfully added to Block");
        return true;
    }*/
    public boolean addmp3Infos(CopyOnWriteArrayList<Song> mp3Infos){
        //if(mp3Info==null)return false;
        //mp3Infos.add(mp3Info);
        this.mp3Infos=mp3Infos;
        System.out.println("Mp3Info Successfully added to Block");
        return true;
    }
}
