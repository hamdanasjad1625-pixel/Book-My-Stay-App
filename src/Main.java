import java.util.*;

/**
 * UseCase6RoomAllocationService
 *
 * This class demonstrates reservation confirmation and safe room allocation
 * using Queue, HashMap, and Set to prevent double booking.
 *
 * @author Asjad
 * @version 6.0
 */

// -------------------- Reservation --------------------
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// -------------------- Inventory Service --------------------
class RoomInventory {
    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void reduceAvailability(String roomType) {
        int current = inventory.getOrDefault(roomType, 0);
        if (current > 0) {
            inventory.put(roomType, current - 1);
        }
    }

    public void displayInventory() {
        System.out.println("\n---- Current Inventory ----");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

// -------------------- Booking Service --------------------
class BookingService {

    private Queue<Reservation> queue;

    // Track allocated room IDs (global uniqueness)
    private Set<String> allocatedRoomIds;

    // Map room type → assigned room IDs
    private HashMap<String, Set<String>> allocationMap;

    private RoomInventory inventory;

    private int roomCounter = 1;

    public BookingService(RoomInventory inventory) {
        this.queue = new LinkedList<>();
        this.allocatedRoomIds = new HashSet<>();
        this.allocationMap = new HashMap<>();
        this.inventory = inventory;
    }

    // Add request
    public void addRequest(Reservation r) {
        queue.offer(r);
    }

    // Process queue (FIFO)
    public void processBookings() {

        System.out.println("---- Processing Booking Requests ----");

        while (!queue.isEmpty()) {

            Reservation r = queue.poll(); // FIFO
            String roomType = r.getRoomType();

            System.out.println("\nProcessing request for " + r.getGuestName());

            // Check availability
            if (inventory.getAvailability(roomType) > 0) {

                // Generate unique room ID
                String roomId;
                do {
                    roomId = roomType.replace(" ", "").toUpperCase() + "-" + roomCounter++;
                } while (allocatedRoomIds.contains(roomId));

                // Store unique ID
                allocatedRoomIds.add(roomId);

                // Map room type → IDs
                allocationMap.putIfAbsent(roomType, new HashSet<>());
                allocationMap.get(roomType).add(roomId);

                // Reduce inventory (atomic step)
                inventory.reduceAvailability(roomType);

                System.out.println("Booking CONFIRMED for " + r.getGuestName());
                System.out.println("Allocated Room ID: " + roomId);

            } else {
                System.out.println("Booking FAILED for " + r.getGuestName() + " (No availability)");
            }
        }
    }

    // Display allocations
    public void displayAllocations() {
        System.out.println("\n---- Room Allocations ----");
        for (Map.Entry<String, Set<String>> entry : allocationMap.entrySet()) {
            System.out.println(entry.getKey() + " → " + entry.getValue());
        }
    }
}

// -------------------- Main Class --------------------
public class UseCase6RoomAllocationService {

    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println("   Book My Stay App");
        System.out.println("   Hotel Booking System v6.0");
        System.out.println("=====================================\n");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Initialize booking service
        BookingService bookingService = new BookingService(inventory);

        // Add booking requests (FIFO)
        bookingService.addRequest(new Reservation("Alice", "Single Room"));
        bookingService.addRequest(new Reservation("Bob", "Single Room"));
        bookingService.addRequest(new Reservation("Charlie", "Single Room")); // should fail
        bookingService.addRequest(new Reservation("David", "Suite Room"));

        // Process bookings
        bookingService.processBookings();

        // Show results
        bookingService.displayAllocations();
        inventory.displayInventory();

        System.out.println("\nAll bookings processed safely.");
    }
}