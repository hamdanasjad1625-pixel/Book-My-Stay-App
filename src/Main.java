import java.util.*;

/**
 * UseCase11ConcurrentBookingSimulation
 *
 * This class demonstrates thread-safe booking using synchronization
 * to avoid race conditions and double booking.
 *
 * @author Asjad
 * @version 11.0
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

// -------------------- Thread-Safe Inventory --------------------
class RoomInventory {
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 2);
    }

    // Critical section (synchronized)
    public synchronized boolean allocateRoom(String roomType) {

        int available = inventory.getOrDefault(roomType, 0);

        if (available > 0) {
            // Simulate delay (to expose race condition if unsynchronized)
            try { Thread.sleep(100); } catch (InterruptedException e) {}

            inventory.put(roomType, available - 1);
            return true;
        }
        return false;
    }

    public void displayInventory() {
        System.out.println("\nFinal Inventory: " + inventory);
    }
}

// -------------------- Booking Processor (Thread) --------------------
class BookingProcessor extends Thread {

    private Queue<Reservation> queue;
    private RoomInventory inventory;

    public BookingProcessor(Queue<Reservation> queue, RoomInventory inventory) {
        this.queue = queue;
        this.inventory = inventory;
    }

    @Override
    public void run() {

        while (true) {
            Reservation r;

            // Synchronized queue access
            synchronized (queue) {
                if (queue.isEmpty()) break;
                r = queue.poll();
            }

            if (r != null) {
                System.out.println(Thread.currentThread().getName() +
                        " processing " + r.getGuestName());

                boolean success = inventory.allocateRoom(r.getRoomType());

                if (success) {
                    System.out.println("Booking SUCCESS for " + r.getGuestName());
                } else {
                    System.out.println("Booking FAILED for " + r.getGuestName());
                }
            }
        }
    }
}

// -------------------- Main Class --------------------
public class UseCase11ConcurrentBookingSimulation {

    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println("   Book My Stay App");
        System.out.println("   Hotel Booking System v11.0");
        System.out.println("=====================================\n");

        // Shared resources
        Queue<Reservation> bookingQueue = new LinkedList<>();
        RoomInventory inventory = new RoomInventory();

        // Simulate multiple booking requests
        bookingQueue.offer(new Reservation("Alice", "Single Room"));
        bookingQueue.offer(new Reservation("Bob", "Single Room"));
        bookingQueue.offer(new Reservation("Charlie", "Single Room"));
        bookingQueue.offer(new Reservation("David", "Single Room"));

        // Create multiple threads
        BookingProcessor t1 = new BookingProcessor(bookingQueue, inventory);
        BookingProcessor t2 = new BookingProcessor(bookingQueue, inventory);

        t1.setName("Thread-1");
        t2.setName("Thread-2");

        // Start threads
        t1.start();
        t2.start();

        // Wait for completion
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Final state
        inventory.displayInventory();

        System.out.println("\nAll bookings processed safely with thread synchronization.");
    }
}