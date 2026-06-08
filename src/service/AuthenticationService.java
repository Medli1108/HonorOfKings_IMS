package service;

import model.Admin;
import model.Person;
import model.Player;

public class AuthenticationService {
    
    private final GameDataManager dataManager;

    public AuthenticationService() {
        this.dataManager = GameDataManager.getInstance();
    }

    public Person authenticateUser(String nameOrId) {
        if (nameOrId == null || nameOrId.trim().isEmpty()) {
            return null;
        }

        // Check Admins first
            for (Admin admin : dataManager.getAdmins()) {
                if (admin.getId().equalsIgnoreCase(nameOrId) || admin.getName().equalsIgnoreCase(nameOrId)) {
                    return admin;
                }
            }

        // Check Players
            for (Player player : dataManager.getPlayers()) {
                if (player.getId().equalsIgnoreCase(nameOrId) || player.getName().equalsIgnoreCase(nameOrId)) {
                    return player;
                }
            }

        return null;
    }
}