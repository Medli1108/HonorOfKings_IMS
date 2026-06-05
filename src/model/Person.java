package model;

import java.util.UUID;

public abstract class Person {
    private final String id = UUID.randomUUID().toString();
    private String name;
    private boolean role; // true for admin, false for normal player.

    protected Person(String name, boolean role) {
        this.name = name;
        this.role = role;
    }

    public String getName() {
        return this.name;
    }

    public String getId() {
        return this.id;
    }
    public boolean getRole() {
        return this.role;
    }

}