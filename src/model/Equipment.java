package model;

import java.util.UUID;

public class Equipment implements Searchable {
    private String id;
    private String name;
    
    // Statistics metrics (Requirement: Rank equipment by usage, win-rate, etc.)
    private int usageCount;
    private double winRate;
    private double averageRating;
    private int wins;

    // Minimal constructor to satisfy DataInitializer
    public Equipment(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.usageCount = 0;
        this.winRate = 0.0;
        this.averageRating = 0.0;
        this.wins = 0;
    }

    // Constructor for File I/O
    public Equipment(String id, String name, int usageCount, double winRate, double averageRating, int wins) {
        this.id = id;
        this.name = name;
        this.usageCount = usageCount;
        this.winRate = winRate;
        this.averageRating = averageRating;
        this.wins = wins;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUsageCount() {
        return usageCount;
    }

    public void setUsageCount(int usageCount) {
        this.usageCount = usageCount;
    }

    public double getWinRate() {
        return winRate;
    }

    public void setWinRate(double winRate) {
        this.winRate = winRate;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }
}

