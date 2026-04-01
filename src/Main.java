import java.io.*;
import java.util.*;

/**
 * UseCase12DataPersistenceRecovery
 *
 * This class demonstrates persistence using serialization and
 * recovery using deserialization.
 *
 * @author Asjad
 * @version 12.0
 */

// -------------------- Reservation --------------------
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getRoomType() {
        return roomType;
    }

    public void display() {
        System.out.println(reservationId + " | " + guestName + " | " + roomType);
    }
}

// -------------------- System State --------------------
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    Map<String, Integer> inventory;
    List<Reservation> bookingHistory;

    public SystemState(Map<String, Integer> inventory, List<Reservation> bookingHistory) {
        this.inventory = inventory;
        this.bookingHistory = bookingHistory;
    }
}

// -------------------- Persistence Service --------------------
class PersistenceService {

    private static final String FILE_NAME = "hotel_state.dat";

    // Save state to file
    public void saveState(SystemState state) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(state);
            System.out.println("System state saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving state: " + e.getMessage());
        }
    }

    // Load state from file
    public SystemState loadState() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            System.out.println("System state loaded successfully.");
            return (SystemState) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("No previous state found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading state. Starting with safe defaults.");
        }
        return null;
    }
}

// -------------------- Main Class --------------------
public class UseCase12DataPersistenceRecovery {

    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println("   Book My Stay App");
        System.out.println("   Hotel Booking System v12.0");
        System.out.println("=====================================\n");

        PersistenceService service = new PersistenceService();

        // Try loading existing state
        SystemState state = service.loadState();

        Map<String, Integer> inventory;
        List<Reservation> history;

        if (state == null) {
            // Fresh start
            inventory = new HashMap<>();
            inventory.put("Single Room", 2);
            inventory.put("Double Room", 1);

            history = new ArrayList<>();

            // Add sample booking
            history.add(new Reservation("SINGLEROOM-1", "Alice", "Single Room"));

        } else {
            // Restore previous state
            inventory = state.inventory;
            history = state.bookingHistory;
        }

        // Display current state
        System.out.println("---- Inventory ----");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }

        System.out.println("\n---- Booking History ----");
        for (Reservation r : history) {
            r.display();
        }

        // Simulate new booking
        System.out.println("\nAdding new booking...");
        Reservation newBooking = new Reservation("DOUBLEROOM-2", "Bob", "Double Room");
        history.add(newBooking);

        inventory.put("Double Room", inventory.get("Double Room") - 1);

        // Save updated state
        SystemState newState = new SystemState(inventory, history);
        service.saveState(newState);

        System.out.println("\nSystem ready for next restart with persisted data.");
    }
}