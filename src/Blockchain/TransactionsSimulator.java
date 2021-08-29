package Blockchain;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TransactionsSimulator implements Runnable {

    private Blockchain blockchain;
    //Singleton Pattern
    public volatile static TransactionsSimulator INSTANCE = new TransactionsSimulator();
    public static List<String> entities = List.of("Ali", "Ahmed", "Sarah","Sherry");
    public static Map<String, TransactionEntity> transactionEntities;

    public static TransactionsSimulator getInstance(Blockchain blockchain){
        INSTANCE.blockchain = blockchain;
        return INSTANCE;
    }

    public void startRunning(){
        transactionEntities = new HashMap<>();
        entities.forEach(name -> transactionEntities.put(name, new TransactionEntity(blockchain, name)));
        transactionEntities.values().forEach(value -> {
            try {
                value.generateKeys(1024);
                value.createKeys();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchProviderException e) {
                e.printStackTrace();
            }
        });
    }

    public void generateRandomTransaction() throws Exception {
        TransactionEntity Payor = transactionEntities.get(entities.get(new Random().nextInt(3)+1));
        String Payee = entities.get(new Random().nextInt(3)+1);
        int amount = (new Random().nextInt(9)+1)*10;
        blockchain.send(Payor, Payee,
                amount, Payor.sign(Payor.getName()+Payee+amount));
    }


    @Override
    public void run() {
        startRunning();
        while(!MinersSimulator.minersStopped){
            try {
                generateRandomTransaction();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
