import java.util.*;

/**
 * UseCase9ErrorHandlingValidation
 *
 * This class demonstrates validation and error handling using
 * custom exceptions and fail-fast design.
 *
 * @author Asjad
 * @version 9.0
 */

// -------------------- Custom Exception --------------------
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

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

// -------------------- Inventory --------------------
class RoomInventory {
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 0); // No availability
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, -1);
    }

    public void reduceAvailability(String roomType) throws InvalidBookingException {
        int current = getAvailability(roomType);

        if (current < 0) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }

        if (current == 0) {
            throw new InvalidBookingException("No availability for " + roomType);
        }

        inventory.put(roomType, current - 1);
    }
}

// -------------------- Validator --------------------
class InvalidBookingValidator {

    private RoomInventory inventory;

    public InvalidBookingValidator(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void validate(Reservation r) throws InvalidBookingException {

        // Validate guest name
        if (r.getGuestName() == null || r.getGuestName().trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }

        // Validate room type
        int availability = inventory.getAvailability(r.getRoomType());

        if (availability == -1) {
            throw new InvalidBookingException("Invalid room type selected: " + r.getRoomType());
        }

        if (availability == 0) {
            throw new InvalidBookingException("Selected room is not available.");
        }
    }
}

// -------------------- Booking Service --------------------
class BookingService {

    private RoomInventory inventory;
    private InvalidBookingValidator validator;
    private int counter = 1;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        this.validator = new InvalidBookingValidator(inventory);
    }

    public void processBooking(Reservation r) {

        try {
            // Validate first (Fail-Fast)
            validator.validate(r);

            // Generate room ID
            String roomId = r.getRoomType().replace(" ", "").toUpperCase() + "-" + counter++;

            // Reduce inventory
            inventory.reduceAvailability(r.getRoomType());

            System.out.println("Booking CONFIRMED for " + r.getGuestName());
            System.out.println("Room ID: " + roomId + "\n");

        } catch (InvalidBookingException e) {
            // Graceful failure
            System.out.println("Booking FAILED for " + r.getGuestName());
            System.out.println("Reason: " + e.getMessage() + "\n");
        }
    }
}

// -------------------- Main Class --------------------
public class UseCase9ErrorHandlingValidation {

    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println("   Book My Stay App");
        System.out.println("   Hotel Booking System v9.0");
        System.out.println("=====================================\n");

        RoomInventory inventory = new RoomInventory();
        BookingService service = new BookingService(inventory);

        // Test cases (valid + invalid)
        Reservation r1 = new Reservation("Alice", "Single Room");   // valid
        Reservation r2 = new Reservation("", "Double Room");        // invalid name
        Reservation r3 = new Reservation("Bob", "Suite Room");      // no availability
        Reservation r4 = new Reservation("Charlie", "Luxury Room"); // invalid type

        // Process bookings
        service.processBooking(r1);
        service.processBooking(r2);
        service.processBooking(r3);
        service.processBooking(r4);

        System.out.println("System remains stable after handling errors.");
    }
}