package Blockchain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MinersSimulator {

    private Blockchain blockchain;
    //Singleton Pattern
    public static MinersSimulator INSTANCE = new MinersSimulator();
    public static List<String> minerNames = List.of("miner1", "miner2", "miner3","miner4", "miner5", "miner6", "miner7", "miner8", "miner9", "miner10");
    final int threads = 10;
    public static Map<String, Miner> miners ;
    ExecutorService executorService;
    public static boolean minersStopped = false;

    public static MinersSimulator getInstance(Blockchain blockchain){
        INSTANCE.blockchain = blockchain;
        return INSTANCE;
    }

    public void startRunning(){
        miners = new HashMap<>();
        executorService = Executors.newFixedThreadPool(threads);
        minerNames.forEach(name -> miners.put(name, new Miner(blockchain, name)));
        miners.values().forEach(executorService::submit);
    }

    public void stop() {
        try {
            executorService.shutdown();
            while (!executorService.isTerminated())
                executorService.awaitTermination(1000, TimeUnit.MILLISECONDS);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        minersStopped = true;
    }

}
