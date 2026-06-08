package model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Hero implements Searchable {
    private String id;
    private String name;
    private HeroType type;
    
    // Base stats
    private int baseHp;
    private int baseAttack;
    
    // Associations
    private List<Equipment> compatibleEquipments;
    private List<Equipment> recommendedEquipments;

    // Minimal constructor to satisfy DataInitializer
    public Hero(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.compatibleEquipments = new ArrayList<>();
        this.recommendedEquipments = new ArrayList<>();
    }

    // Getters and setters
    public String getId() {
        return this.id;
    }
    public String getName() {
        return this.name;
    }
    public HeroType getType() {
        return this.type;
    }
    public int getBaseHp() {
        return this.baseHp;
    }
    public int getBaseAttack() {
        return this.baseAttack;
    }
    public List<Equipment> getCompatibleEquipments() {
        return compatibleEquipments;
    }
    public List<Equipment> getRecommendedEquipments() {
        return recommendedEquipments;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setType(HeroType type) {
        this.type = type;
    }
    public void setBaseHp(int baseHp) {
        this.baseHp = baseHp;
    }
    public void setBaseAttack(int baseAttack) {
        this.baseAttack = baseAttack;
    }

    // Methods
    public void addCompatibleEquipment(Equipment equipment) {
        this.compatibleEquipments.add(equipment);
    }
    public void addRecommendedEquipment(Equipment equipment) {
        this.recommendedEquipments.add(equipment);
    }

    // File I/O constructor
    public Hero(String id, String name, HeroType type, int baseHp, int baseAttack, List<Equipment> compatibleEquipments, ArrayList<Equipment> recommendedEquipments) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.baseHp = baseHp;
        this.baseAttack = baseAttack;
        this.compatibleEquipments = compatibleEquipments;
        this.recommendedEquipments = recommendedEquipments;
    }


}