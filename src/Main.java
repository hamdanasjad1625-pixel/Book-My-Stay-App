import java.util.*;

/**
 * UseCase10BookingCancellation
 *
 * This class demonstrates booking cancellation with safe rollback
 * using Stack (LIFO) and controlled state restoration.
 *
 * @author Asjad
 * @version 10.0
 */

// -------------------- Reservation --------------------
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private boolean isActive;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.isActive = true;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getRoomType() {
        return roomType;
    }

    public boolean isActive() {
        return isActive;
    }

    public void cancel() {
        isActive = false;
    }

    public void display() {
        System.out.println(reservationId + " | " + guestName + " | " + roomType +
                " | Status: " + (isActive ? "Active" : "Cancelled"));
    }
}

// -------------------- Inventory --------------------
class RoomInventory {
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 1);
        inventory.put("Double Room", 1);
    }

    public void increaseAvailability(String roomType) {
        inventory.put(roomType, inventory.getOrDefault(roomType, 0) + 1);
    }

    public void displayInventory() {
        System.out.println("\n---- Inventory ----");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

// -------------------- Cancellation Service --------------------
class CancellationService {

    // Store reservations
    private Map<String, Reservation> reservationMap;

    // Stack for rollback (LIFO)
    private Stack<String> rollbackStack;

    private RoomInventory inventory;

    public CancellationService(RoomInventory inventory) {
        this.inventory = inventory;
        this.reservationMap = new HashMap<>();
        this.rollbackStack = new Stack<>();
    }

    // Add confirmed reservation (simulate previous booking)
    public void addReservation(Reservation r) {
        reservationMap.put(r.getReservationId(), r);
    }

    // Cancel booking
    public void cancelReservation(String reservationId) {

        System.out.println("\nProcessing cancellation for: " + reservationId);

        // Validate existence
        if (!reservationMap.containsKey(reservationId)) {
            System.out.println("Cancellation FAILED: Reservation not found.");
            return;
        }

        Reservation r = reservationMap.get(reservationId);

        // Check if already cancelled
        if (!r.isActive()) {
            System.out.println("Cancellation FAILED: Already cancelled.");
            return;
        }

        // Push to rollback stack
        rollbackStack.push(reservationId);

        // Restore inventory
        inventory.increaseAvailability(r.getRoomType());

        // Mark as cancelled
        r.cancel();

        System.out.println("Cancellation SUCCESS for " + reservationId);
    }

    // Show rollback history
    public void displayRollbackStack() {
        System.out.println("\n---- Rollback Stack (Recent First) ----");
        for (int i = rollbackStack.size() - 1; i >= 0; i--) {
            System.out.println(rollbackStack.get(i));
        }
    }

    // Show all reservations
    public void displayReservations() {
        System.out.println("\n---- Reservations ----");
        for (Reservation r : reservationMap.values()) {
            r.display();
        }
    }
}

// -------------------- Main Class --------------------
public class UseCase10BookingCancellation {

    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println("   Book My Stay App");
        System.out.println("   Hotel Booking System v10.0");
        System.out.println("=====================================");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Initialize cancellation service
        CancellationService service = new CancellationService(inventory);

        // Simulate confirmed bookings
        Reservation r1 = new Reservation("SINGLEROOM-1", "Alice", "Single Room");
        Reservation r2 = new Reservation("DOUBLEROOM-2", "Bob", "Double Room");

        service.addReservation(r1);
        service.addReservation(r2);

        // Display initial state
        service.displayReservations();
        inventory.displayInventory();

        // Perform cancellations
        service.cancelReservation("SINGLEROOM-1");
        service.cancelReservation("SINGLEROOM-1"); // duplicate cancel
        service.cancelReservation("INVALID-ID");   // invalid cancel

        // Display final state
        service.displayReservations();
        inventory.displayInventory();
        service.displayRollbackStack();

        System.out.println("\nSystem state restored consistently after cancellations.");
    }
}