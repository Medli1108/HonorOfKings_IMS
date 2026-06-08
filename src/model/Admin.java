package model;

public class Admin extends Person {
    public Admin(String name) {
        super(name, Role.ADMIN);
    }

    public Admin(String id, String name) {
        super(id, name, Role.ADMIN);
    }
}