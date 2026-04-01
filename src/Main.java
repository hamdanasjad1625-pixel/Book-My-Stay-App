import java.util.*;

/**
 * UseCase8BookingHistoryReport
 *
 * This class demonstrates booking history tracking and reporting
 * using List to maintain ordered records of confirmed reservations.
 *
 * @author Asjad
 * @version 8.0
 */

// -------------------- Reservation --------------------
class Reservation {
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

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void display() {
        System.out.println("Reservation ID: " + reservationId +
                ", Guest: " + guestName +
                ", Room: " + roomType);
    }
}

// -------------------- Booking History --------------------
class BookingHistory {

    // List to maintain insertion order
    private List<Reservation> history;

    public BookingHistory() {
        history = new ArrayList<>();
    }

    // Add confirmed booking
    public void addReservation(Reservation r) {
        history.add(r);
    }

    // Get all reservations (read-only usage)
    public List<Reservation> getAllReservations() {
        return history;
    }
}

// -------------------- Report Service --------------------
class BookingReportService {

    // Display all bookings
    public void displayAllBookings(List<Reservation> reservations) {
        System.out.println("\n---- Booking History ----");

        if (reservations.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        for (Reservation r : reservations) {
            r.display();
        }
    }

    // Generate summary report
    public void generateSummary(List<Reservation> reservations) {
        System.out.println("\n---- Booking Summary Report ----");

        Map<String, Integer> roomCount = new HashMap<>();

        for (Reservation r : reservations) {
            String roomType = r.getRoomType();
            roomCount.put(roomType, roomCount.getOrDefault(roomType, 0) + 1);
        }

        for (Map.Entry<String, Integer> entry : roomCount.entrySet()) {
            System.out.println(entry.getKey() + " Bookings: " + entry.getValue());
        }

        System.out.println("Total Bookings: " + reservations.size());
    }
}

// -------------------- Main Class --------------------
public class UseCase8BookingHistoryReport {

    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println("   Book My Stay App");
        System.out.println("   Hotel Booking System v8.0");
        System.out.println("=====================================\n");

        // Initialize booking history
        BookingHistory history = new BookingHistory();

        // Simulate confirmed bookings (from Use Case 6)
        history.addReservation(new Reservation("SINGLEROOM-1", "Alice", "Single Room"));
        history.addReservation(new Reservation("DOUBLEROOM-2", "Bob", "Double Room"));
        history.addReservation(new Reservation("SUITEROOM-3", "Charlie", "Suite Room"));
        history.addReservation(new Reservation("SINGLEROOM-4", "David", "Single Room"));

        // Initialize report service
        BookingReportService reportService = new BookingReportService();

        // Display booking history
        reportService.displayAllBookings(history.getAllReservations());

        // Generate summary report
        reportService.generateSummary(history.getAllReservations());

        System.out.println("\nReporting completed. No data was modified.");
    }
}