import java.util.HashMap;
import java.util.Map;

/**
 * UseCase3InventorySetup
 *
 * This class demonstrates centralized room inventory management
 * using HashMap as a single source of truth.
 *
 * @author Asjad
 * @version 3.1
 */

// Inventory Class
class RoomInventory {

    // HashMap to store room type and availability
    private HashMap<String, Integer> inventory;

    // Constructor to initialize inventory
    public RoomInventory() {
        inventory = new HashMap<>();

        // Initial room availability
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    // Method to get availability of a room type
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Method to update availability (add/remove rooms)
    public void updateAvailability(String roomType, int countChange) {
        int current = inventory.getOrDefault(roomType, 0);
        int updated = current + countChange;

        if (updated >= 0) {
            inventory.put(roomType, updated);
        } else {
            System.out.println("Error: Cannot reduce below zero for " + roomType);
        }
    }

    // Method to display full inventory
    public void displayInventory() {
        System.out.println("---- Current Room Inventory ----");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
        System.out.println();
    }
}

// Main Class
public class UseCase3InventorySetup {

    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println("   Book My Stay App");
        System.out.println("   Hotel Booking System v3.1");
        System.out.println("=====================================\n");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Display initial inventory
        inventory.displayInventory();

        // Retrieve availability
        System.out.println("Available Single Rooms: " + inventory.getAvailability("Single Room"));
        System.out.println();

        // Update inventory (simulate booking/cancellation)
        System.out.println("Booking 2 Single Rooms...");
        inventory.updateAvailability("Single Room", -2);

        System.out.println("Adding 1 Suite Room...");
        inventory.updateAvailability("Suite Room", +1);

        System.out.println();

        // Display updated inventory
        inventory.displayInventory();

        System.out.println("Application finished successfully.");
    }
}