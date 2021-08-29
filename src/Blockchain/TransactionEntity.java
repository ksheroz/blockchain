package Blockchain;

import java.security.*;
import java.util.Random;

public class TransactionEntity {

    private Blockchain blockchain;
    private String name;
    private long maxSleepTimer = 150;
    private boolean terminate = false;
    private int amount = 200;
    private KeyPairGenerator keyGen;
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private KeyPair pair;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public TransactionEntity(Blockchain blockchain, String name) {
        this.blockchain = blockchain;
        this.name = name;
    }

    public String getName() {
        return name;
    }



    public void generateKeys(int keylength) throws NoSuchAlgorithmException, NoSuchProviderException {

        this.keyGen = KeyPairGenerator.getInstance("RSA");
        this.keyGen.initialize(keylength);
    }

    public void createKeys() {
        this.pair = this.keyGen.generateKeyPair();
        this.privateKey = pair.getPrivate();
        this.publicKey = pair.getPublic();

    }

    public PrivateKey getPrivateKey() {
        return this.privateKey;
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    public byte[] sign(String data) throws InvalidKeyException, Exception{
        Signature rsa = Signature.getInstance("SHA1withRSA");
        rsa.initSign(this.getPrivateKey());
        rsa.update(data.getBytes());
        return rsa.sign();
    }


}
