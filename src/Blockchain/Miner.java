package Blockchain;

import java.util.Random;
import java.util.Scanner;
import java.lang.*;
import java.util.concurrent.Callable;

public class Miner implements Runnable{

    private Blockchain blockchain;
    private String name;
    private int amount = 100;


    public Miner(Blockchain blockchain, String name){
        this.blockchain=blockchain;
        this.name = name;
    }

    public void run(){
        Thread.currentThread().setName(name);

        TransactionsSimulator transactionsSimulator = TransactionsSimulator.getInstance(blockchain);
        while(blockchain.blocksMined() != blockchain.requiredBlocks){
            try {
                transactionsSimulator.generateRandomTransaction();
            } catch (Exception e) {
                e.printStackTrace();
            }
            blockchain.validateNewBlock(blockchain.newBlock());
        }
    }
    public void withdraw(int VC){
        amount -= VC;
    }

    public void deposit(int VC){
        amount += VC;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
