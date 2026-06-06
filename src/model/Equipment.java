package model;

import java.util.UUID;

public class Equipment {
    private String id;
    private String name;
    
    // Statistics metrics (Requirement: Rank equipment by usage, win-rate, etc.)
    private int usageCount;
    private double winRateContribution;
    private double averageRating;

    // Minimal constructor to satisfy DataInitializer
    public Equipment(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.usageCount = 0;
        this.winRateContribution = 0.0;
        this.averageRating = 0.0;
    }

    // Constructor for File I/O
    public Equipment(String id, String name, int usageCount, double winRateContribution, double averageRating) {
        this.id = id;
        this.name = name;
        this.usageCount = usageCount;
        this.winRateContribution = winRateContribution;
        this.averageRating = averageRating;
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

    public double getWinRateContribution() {
        return winRateContribution;
    }

    public void setWinRateContribution(double winRateContribution) {
        this.winRateContribution = winRateContribution;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }
}
