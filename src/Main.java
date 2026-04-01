import java.util.*;

/**
 * UseCase7AddOnServiceSelection
 *
 * This class demonstrates how add-on services can be attached to reservations
 * without modifying booking or inventory logic.
 *
 * @author Asjad
 * @version 7.0
 */

// -------------------- Add-On Service --------------------
class AddOnService {
    private String serviceName;
    private double cost;

    public AddOnService(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getCost() {
        return cost;
    }
}

// -------------------- Add-On Service Manager --------------------
class AddOnServiceManager {

    // Map Reservation ID → List of Services
    private Map<String, List<AddOnService>> serviceMap;

    public AddOnServiceManager() {
        serviceMap = new HashMap<>();
    }

    // Add service to a reservation
    public void addService(String reservationId, AddOnService service) {
        serviceMap.putIfAbsent(reservationId, new ArrayList<>());
        serviceMap.get(reservationId).add(service);

        System.out.println(service.getServiceName() + " added to Reservation " + reservationId);
    }

    // Display services for a reservation
    public void displayServices(String reservationId) {
        System.out.println("\nServices for Reservation " + reservationId + ":");

        List<AddOnService> services = serviceMap.get(reservationId);

        if (services == null || services.isEmpty()) {
            System.out.println("No add-on services selected.");
            return;
        }

        for (AddOnService s : services) {
            System.out.println("- " + s.getServiceName() + " (₹" + s.getCost() + ")");
        }
    }

    // Calculate total cost of services
    public double calculateTotalCost(String reservationId) {
        List<AddOnService> services = serviceMap.get(reservationId);

        if (services == null) return 0;

        double total = 0;
        for (AddOnService s : services) {
            total += s.getCost();
        }
        return total;
    }
}

// -------------------- Main Class --------------------
public class UseCase7AddOnServiceSelection {

    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println("   Book My Stay App");
        System.out.println("   Hotel Booking System v7.0");
        System.out.println("=====================================\n");

        // Assume reservation already exists (from Use Case 6)
        String reservationId = "SINGLEROOM-1";

        // Initialize service manager
        AddOnServiceManager manager = new AddOnServiceManager();

        // Create add-on services
        AddOnService breakfast = new AddOnService("Breakfast", 500);
        AddOnService wifi = new AddOnService("WiFi", 200);
        AddOnService airportPickup = new AddOnService("Airport Pickup", 800);

        // Guest selects services
        manager.addService(reservationId, breakfast);
        manager.addService(reservationId, wifi);
        manager.addService(reservationId, airportPickup);

        // Display selected services
        manager.displayServices(reservationId);

        // Calculate total add-on cost
        double totalCost = manager.calculateTotalCost(reservationId);
        System.out.println("\nTotal Add-On Cost: ₹" + totalCost);

        System.out.println("\nCore booking and inventory remain unchanged.");
    }
}