package model;

public class Admin extends Person {
    public Admin(String name) {
        super(name, Role.ADMIN);
    }

    // Constructor for loading an existing admin from storage
    public Admin(String id, String name) {
        super(id, name, Role.ADMIN);
    }
}