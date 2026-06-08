package model;

import java.util.UUID;

public abstract class Person implements Searchable {
    private String id;
    private String name;
    private Role role; 

    // Constructor for new instances
    protected Person(String name, Role role) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.role = role;
    }

    // Constructor for loading existing instances from storage
    protected Person(String id, String name, Role role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }

    public String getName() {
        return this.name;
    }

    public String getId() {
        return this.id;
    }
    
    public Role getRole() {
        return this.role;
    }

    public void setName(String name) {
        this.name = name;
    }
}