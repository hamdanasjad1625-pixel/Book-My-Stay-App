import java.util.LinkedList;
import java.util.Queue;

/**
 * UseCase5BookingRequestQueue
 *
 * This class demonstrates handling booking requests using a Queue
 * to ensure First-Come-First-Served (FIFO) processing.
 *
 * No inventory updates or allocations are performed here.
 *
 * @author Asjad
 * @version 5.0
 */

// -------------------- Reservation Class --------------------
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

    public void display() {
        System.out.println("Guest: " + guestName + ", Requested Room: " + roomType);
    }
}

// -------------------- Booking Queue --------------------
class BookingRequestQueue {

    private Queue<Reservation> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    // Add booking request (enqueue)
    public void addRequest(Reservation reservation) {
        queue.offer(reservation);
        System.out.println("Request added for " + reservation.getGuestName());
    }

    // View all requests (without removing)
    public void displayQueue() {
        System.out.println("\n---- Booking Request Queue (FIFO Order) ----");

        if (queue.isEmpty()) {
            System.out.println("No booking requests.");
            return;
        }

        for (Reservation r : queue) {
            r.display();
        }
    }
}

// -------------------- Main Class --------------------
public class UseCase5BookingRequestQueue {

    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println("   Book My Stay App");
        System.out.println("   Hotel Booking System v5.0");
        System.out.println("=====================================\n");

        // Initialize queue
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        // Simulate booking requests
        Reservation r1 = new Reservation("Alice", "Single Room");
        Reservation r2 = new Reservation("Bob", "Double Room");
        Reservation r3 = new Reservation("Charlie", "Suite Room");

        // Add requests (FIFO order)
        bookingQueue.addRequest(r1);
        bookingQueue.addRequest(r2);
        bookingQueue.addRequest(r3);

        // Display queue (no processing yet)
        bookingQueue.displayQueue();

        System.out.println("\nAll requests are stored and waiting for processing.");
        System.out.println("No inventory changes have been made.");
    }
}