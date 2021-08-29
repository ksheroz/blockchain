package Blockchain;

import java.io.Serializable;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.security.*;
import java.time.Duration;
import java.time.LocalTime;
import java.util.*;
import java.lang.*;

public class Blockchain implements Serializable {

    private static final long serialVersionUID = 9837L;
    private volatile List<Block> blocks = new ArrayList<>();
    private volatile int zeros;
    public static String filename = "./blockchaininfo.txt";
    private String zerosStatus;
    private volatile int idx = 0;
    private String prevHash = "0";
    public int requiredBlocks = 5;
    private volatile Map<String, Integer> transactions = new HashMap<>();
    private volatile List<String> transactionString = new ArrayList<>();
    private volatile static int uniqueIdentifier = 1;
    private int currentlyMining = 1;


    protected Block newBlock() {
        Block newBlock = new Block();
        newBlock.setPrevblockHash(prevHash);
        int proofOfWork = new Random().nextInt(Integer.MAX_VALUE);
        while (!StringUtil.applySha256(Integer.toString(idx + 1) + newBlock.getPrevblockHash() + proofOfWork).substring(0, zeros).equals("0".repeat(zeros))) {
            proofOfWork = new Random().nextInt(Integer.MAX_VALUE);
        }
        newBlock.setBlockHash(StringUtil.applySha256(Integer.toString(idx + 1) + newBlock.getPrevblockHash() + proofOfWork));
        newBlock.setProofOfWork(proofOfWork);
        newBlock.setTimestamp(new Date().getTime());
        newBlock.setMinedBy(Thread.currentThread().getName());
        return newBlock;
    }

    protected synchronized boolean validateNewBlock(Block block) {
        if (idx != 0) {
            if (block.getPrevblockHash().equals(blocks.get(blocks.size() - 1).getBlockHash()) && block.getBlockHash().substring(0, zeros).equals("0".repeat(zeros))) {
                LocalTime start = LocalTime.now();
                boolean validity = true;
                block.setId(++idx);
                blocks.add(block);
                List<String> tempList = new ArrayList<>();
                Map<String, Integer> tempMap = new HashMap<>();
                tempMap.putAll(transactions);
                tempList.addAll(transactionString);
                block.setInformation(tempList);
                transactionString.removeAll(tempList);
                initTransaction(transactions);
                transactions.keySet().removeAll(tempMap.keySet());
                int proofOfWork = new Random().nextInt(Integer.MAX_VALUE);
                while (!StringUtil.applySha256(Integer.toString(idx + 1) + block.getPrevblockHash() + proofOfWork + tempList).substring(0, zeros).equals("0".repeat(zeros))) {
                    proofOfWork = new Random().nextInt(Integer.MAX_VALUE);
                }
                block.setBlockHash(StringUtil.applySha256(Integer.toString(idx + 1) + block.getPrevblockHash() + proofOfWork + tempList));
                prevHash = block.getBlockHash();
                System.out.println("another block--------------------------------------------------------------------------------------------------------");
                LocalTime end = LocalTime.now();
                Duration duration = Duration.between(start, end);
                block.setCreationTime((int) Math.abs(duration.toSeconds()));
                regulateZeros(block);
                block.setZerosStatus(zerosStatus);
                return validity;
            } else {
                return false;
            }
        } else {
            if (block.getPrevblockHash() == "0") {
                LocalTime start = LocalTime.now();
                block.setId(++idx);
                blocks.add(block);
                prevHash = block.getBlockHash();
                List<String> tempList = new ArrayList<>();
                tempList.add(Thread.currentThread().getName() + " gets 100 VC");
                block.setInformation(tempList);
                MinersSimulator.miners.get(Thread.currentThread().getName()).deposit(100);
                LocalTime end = LocalTime.now();
                Duration duration = Duration.between(start, end);
                block.setCreationTime((int) Math.abs(duration.toSeconds()));

                regulateZeros(block);
                block.setZerosStatus(zerosStatus);

                return true;
            } else {
                return false;
            }
        }
    }

    private void initTransaction(Map<String, Integer> map) {
        map.keySet().forEach(key -> TransactionsSimulator.transactionEntities.get(key).setAmount(TransactionsSimulator.transactionEntities.get(key).getAmount()+map.get(key)));

    }


    protected boolean validate() {
        String temp = "0";
        for (Block block : blocks) {
            if (!(block.getPrevblockHash().equals(temp))) {
                return false;
            }
            temp = block.getBlockHash();
        }
        return true;
    }

    public String toString() {
        StringBuilder blockchainBuilder = new StringBuilder();
        for (Block block : blocks) {
            blockchainBuilder.append("Block:");
            blockchainBuilder.append("\nCreated by miner # " + block.getMinedBy());
            blockchainBuilder.append("\n"+block.getMinedBy()+" gets 100 VC");
            blockchainBuilder.append("\nId: " + block.getId());
            blockchainBuilder.append("\nTimestamp: " + block.getTimestamp());
            blockchainBuilder.append("\nMagic number: " + block.getProofOfWork());
            blockchainBuilder.append("\nHash of the previous block:\n" + block.getPrevblockHash());
            blockchainBuilder.append("\nHash of the block:\n" + block.getBlockHash());
            blockchainBuilder.append("\nBlock data:\n");
            blockchainBuilder.append(block.getInformation());
            blockchainBuilder.append("Block was generating for " + block.getCreationTime() + " seconds");
            blockchainBuilder.append("\n" + block.getZerosStatus() + "\n\n");
        }
        return blockchainBuilder.toString();
    }

    public int getZeros() {
        return zeros;
    }

    public void setZeros(int zeros) {
        this.zeros = zeros;
    }

    protected void regulateZeros(Block block) {
        if (block.getCreationTime() >= 1 ) {
            zeros--;
            zerosStatus = "N was decreased to 1";
        }

        if(zeros == 4){
            setZeros(0);
        }
        else{
            zeros++;
            zerosStatus ="N was increased to "+zeros;
        }
    }
    public int blocksMined() {
        return blocks.size();
    }

    public synchronized void send(TransactionEntity Payor, String Payee, int amount, byte[] signature) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {

        System.out.println("Transaction caught");
        Signature sig = Signature.getInstance("SHA1withRSA");
        sig.initVerify(Payor.getPublicKey());
        sig.update((Payor.getName()+Payee+amount).getBytes());

        boolean verified = sig.verify(signature);
        int currentDeficit = 0;
        if(transactions.getOrDefault(Payor.getName(), 0) < 0){
            currentDeficit = transactions.get(Payor.getName());
        }


        if(verified && ((Payor.getAmount()+currentDeficit) >= amount)){
            boolean PayeeAlreadyPresent = transactions.containsKey(Payee);
            boolean PayorAlreadyPresent = transactions.containsKey(Payor.getName());
            if(PayeeAlreadyPresent){
                int currentAmount = transactions.get(Payee);
                transactions.put(Payee, currentAmount+amount);
            }
            if(PayorAlreadyPresent){
                int currentAmount = transactions.get(Payor.getName());
                transactions.put(Payor.getName(), currentAmount-amount);
            }
            transactions.putIfAbsent(Payee,amount);
            transactions.putIfAbsent(Payor.getName(), -amount);
            int newUniqueIdentifier = uniqueIdentifier++;
            transactionString.add(newUniqueIdentifier + " " + Payor.getName() + " sent " + amount + " VC to " + Payee);
        }
    }
}
