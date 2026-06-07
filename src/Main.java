import service.GameDataManager;
import service.FileStorageService;
import util.DataInitializer;

public class Main {
    public static void main(String[] args) {
        System.out.println("Application starting...");
        
        GameDataManager dataManager = GameDataManager.getInstance();
        FileStorageService storageService = new FileStorageService();
        
        // Attempt to load existing data
        System.out.println("Loading data...");
        storageService.loadData(dataManager);
        
        // If no data was loaded (lists are empty), initialize with dummy data
        if (dataManager.getPlayers().isEmpty()) {
            System.out.println("Initializing with dummy data...");
            DataInitializer.initialize();
            
            // Save initial data immediately
            System.out.println("Saving initial data...");
            storageService.saveData(dataManager);
        }
        
        System.out.println("Application ready.");
    }
}