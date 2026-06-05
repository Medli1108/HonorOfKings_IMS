package model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Hero {
    private String id;
    private String name;
    private HeroType type;
    
    // Base stats
    private int baseHp;
    private int baseAttack;
    
    // Associations
    private List<Equipment> compatibleEquipment;
    private List<Equipment> recommendedEquipment;

    // Minimal constructor to satisfy DataInitializer
    public Hero(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.compatibleEquipment = new ArrayList<>();
        this.recommendedEquipment = new ArrayList<>();
    }

    // TODO: Implementation Agent to add getters, setters, File I/O constructors, and stat logic
}