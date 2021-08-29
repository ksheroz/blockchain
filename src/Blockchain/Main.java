package Blockchain;

import java.awt.image.VolatileImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.lang.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws Exception {

        try {
            Blockchain blockchain = (Blockchain) SerializationUtils.deserialize(Blockchain.filename);
            System.out.println(blockchain);
            System.out.println(blockchain.validate());
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No existing blockchain found!");
            Blockchain blockchain = new Blockchain();

            TransactionsSimulator transactionsSimulator = TransactionsSimulator.getInstance(blockchain);
            MinersSimulator minersSimulator = MinersSimulator.getInstance(blockchain);

            transactionsSimulator.startRunning();
            minersSimulator.startRunning();
            minersSimulator.stop();
            System.out.println(blockchain);

            System.out.println(blockchain);

            System.out.println(blockchain);

            try {
                SerializationUtils.serialize(blockchain, Blockchain.filename);
            } catch (IOException e2) {
                e2.printStackTrace();
                System.out.println("Object states could not be saved!");
            }
        }
    }

}
