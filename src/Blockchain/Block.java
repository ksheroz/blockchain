package Blockchain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Block implements Serializable {

    private int id;
    private long timestamp;
    private String prevblockHash;
    private String blockHash;
    private List<String> information = new ArrayList<>();
    private int proofOfWork;
    private int creationTime;
    private String minedBy;
    private String zerosStatus;
    private int highestIdentifier;

    public int getHighestIdentifier() {
        return highestIdentifier;
    }

    public void setHighestIdentifier(int highestIdentifier) {
        this.highestIdentifier = highestIdentifier;
    }

    public String getZerosStatus() {
        return zerosStatus;
    }

    public void setZerosStatus(String zerosStatus) {
        this.zerosStatus = zerosStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getPrevblockHash() {
        return prevblockHash;
    }

    public void setPrevblockHash(String prevblockHash) {
        this.prevblockHash = prevblockHash;
    }

    public String getBlockHash() {
        return blockHash;
    }

    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash;
    }

    public String getInformation() {
        StringBuilder informationBuilder = new StringBuilder();
        for(String message: information){
            informationBuilder.append(message);
            informationBuilder.append("\n");
        }
        return informationBuilder.toString();
    }

    public void setInformation(List<String> information) {
        this.information = information;
    }

    public int getProofOfWork() {
        return proofOfWork;
    }

    public void setProofOfWork(int proofOfWork) {
        this.proofOfWork = proofOfWork;
    }

    public int getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(int creationTime) {
        this.creationTime = creationTime;
    }

    public String getMinedBy() {
        return minedBy;
    }

    public void setMinedBy(String minedBy) {
        this.minedBy = minedBy;
    }
}
