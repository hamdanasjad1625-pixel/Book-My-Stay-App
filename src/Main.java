/**
 * UseCase2RoomInitialization
 *
 * This class demonstrates basic room types and static availability
 * using abstraction, inheritance, and polymorphism.
 *
 * @author Asjad
 * @version 2.1
 */

// Abstract class
abstract class Room {
    protected String roomType;
    protected int numberOfBeds;
    protected double price;

    // Constructor
    public Room(String roomType, int numberOfBeds, double price) {
        this.roomType = roomType;
        this.numberOfBeds = numberOfBeds;
        this.price = price;
    }

    // Method to display room details
    public void displayDetails() {
        System.out.println("Room Type: " + roomType);
        System.out.println("Beds: " + numberOfBeds);
        System.out.println("Price per night: ₹" + price);
    }
}

// Single Room
class SingleRoom extends Room {

    public SingleRoom() {
        super("Single Room", 1, 2000);
    }
}

// Double Room
class DoubleRoom extends Room {

    public DoubleRoom() {
        super("Double Room", 2, 3500);
    }
}

// Suite Room
class SuiteRoom extends Room {

    public SuiteRoom() {
        super("Suite Room", 3, 6000);
    }
}

// Main class
public class UseCase2RoomInitialization {

    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println("   Book My Stay App");
        System.out.println("   Hotel Booking System v2.1");
        System.out.println("=====================================\n");

        // Create room objects (Polymorphism)
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Static availability variables
        int singleAvailability = 5;
        int doubleAvailability = 3;
        int suiteAvailability = 2;

        // Display details
        System.out.println("---- Single Room ----");
        single.displayDetails();
        System.out.println("Available: " + singleAvailability + "\n");

        System.out.println("---- Double Room ----");
        doubleRoom.displayDetails();
        System.out.println("Available: " + doubleAvailability + "\n");

        System.out.println("---- Suite Room ----");
        suite.displayDetails();
        System.out.println("Available: " + suiteAvailability + "\n");

        System.out.println("Application finished successfully.");
    }
}