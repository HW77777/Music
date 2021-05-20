package com.example.music;

import android.view.SurfaceControl;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;

public class Wallet extends Thread{
    public PrivateKey privateKey;
    public PublicKey publicKey;

   // public HashMap<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>();

    public Wallet() {
        generateKeyPair();
    }

    @Override
    public void run() {
        super.run();
    }

    public void generateKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator=KeyPairGenerator.getInstance("EC");
            SecureRandom random=new SecureRandom();
            ECGenParameterSpec ecSpec=new ECGenParameterSpec("secp256r1");
            keyPairGenerator.initialize(ecSpec,random);
            KeyPair keyPair=keyPairGenerator.generateKeyPair();
            publicKey=keyPair.getPublic();
            privateKey=keyPair.getPrivate();
        }catch(Exception e) {
            throw new RuntimeException(e);
        }
    }


/*    public Mp3Info setMp3Info(String s){
        Mp3Info mp3Info=new Mp3Info().getSongInfo(publicKey,s);
        mp3Info.generateSignature(privateKey);
        return mp3Info;
    }*/
}
