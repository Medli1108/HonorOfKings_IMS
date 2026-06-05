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

    // TODO: Implementation Agent to add getters, setters, and overloaded constructors for File I/O
}