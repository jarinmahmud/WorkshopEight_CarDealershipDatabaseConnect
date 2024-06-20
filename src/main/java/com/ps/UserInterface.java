package com.ps;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class UserInterface {
    private Dealership dealership;

    //creating object of dealership here
    private void init() {
        DealershipFileManager fileManager = new DealershipFileManager();
        dealership = fileManager.getDealership();
    }

    //Home Prompt
    private void displayMenu() {
        System.out.println("Welcome to Car Dealership CLI Application");
        System.out.println("--------------------------------------------------------");
        System.out.println("1. Find vehicles within a price range");
        System.out.println("2. Find vehicles by make/model");
        System.out.println("3. Find vehicles by year range");
        System.out.println("4. Find vehicles by color");
        System.out.println("5. Find vehicles by mileage range");
        System.out.println("6. Find vehicles by type");
        System.out.println("7. List all vehicles");
        System.out.println("8. Add a vehicle");
        System.out.println("9. Remove a vehicle");
        System.out.println("10. Sell/Lease a Vehicle");
        System.out.println("99. Quit");
        System.out.println("Enter your choice:");
    }
    public void display() {
        init();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            displayMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    processGetByPriceRequest(scanner);
                    break;
                case 2:
                    processGetByMakeModelRequest(scanner);

                    break;
                case 3:
                    processGetByYearRequest(scanner);
                    break;
                case 4:
                    processGetByColorRequest(scanner);
                    break;
                case 5:
                    processGetByMileageRequest(scanner);
                    break;
                case 6:
                    processGetByVehicleTypeRequest(scanner);
                    break;
                case 7:
                    processAllVehiclesRequest();
                    break;
                case 8:
                    processAddVehicleRequest(scanner);
                    break;
                case 9:
                    processRemoveVehicleRequest(scanner);
                    break;
                case 10:
                    System.out.print("Enter date (MMDDYYYY): ");
                    String date = scanner.nextLine();

                    System.out.print("Enter customer name: ");
                    String customerName = scanner.nextLine();

                    System.out.print("Enter customer email: ");
                    String customerEmail = scanner.nextLine();

                    System.out.print("Enter vehicle VIN: ");
                    String vehicleVin = scanner.nextLine();

                    System.out.print("Enter vehicle price: ");
                    double price = scanner.nextDouble();
                    scanner.nextLine();

                    System.out.print("Is this a sale or a lease (sale/lease): ");
                    String contractType = scanner.nextLine();

                    Contract contract = null;

                    if ("sale".equalsIgnoreCase(contractType)) {
                        System.out.print("Do you want to finance the vehicle (yes/no): ");
                        String financeOption = scanner.nextLine();
                        boolean finance = "yes".equalsIgnoreCase(financeOption);

                        contract = new SalesContract(date, customerName, customerEmail, vehicleVin, price, finance);
                    } else if ("lease".equalsIgnoreCase(contractType)) {
                        LocalDate vehicleYear = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyyMMdd"));
                        if (vehicleYear.isBefore(LocalDate.now().minusYears(3))) {
                            System.out.println("You can't lease a vehicle over 3 years old.");
                            continue;
                        }

                        contract = new LeaseContract(date, customerName, customerEmail, vehicleVin, price);
                    } else {
                        System.out.println("Invalid contract type.");
                        continue;
                    }

                    ContractFileManager.saveContract(contract);
                    System.out.println("Contract saved.");
                    // Here you would remove the vehicle from inventory

                    break;
                case 99:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }

    private void displayVehicles(List<Vehicle> vehicles) {
        if (vehicles.isEmpty()) {
            System.out.println("No vehicles found.");
        } else {
            System.out.println("-------------------------------------------------------------");
            System.out.printf("| %-10s | %-4s | %-10s | %-10s | %-10s | %-6s | %-8s |\n",
                    "VIN", "Year", "Make", "Model", "Type", "Color", "Price");
            System.out.println("-------------------------------------------------------------");
            for (Vehicle vehicle : vehicles) {
                System.out.printf("| %-10d | %-4d | %-10s | %-10s | %-10s | %-6s | %-8.2f |\n",
                        vehicle.getVin(), vehicle.getYear(), vehicle.getMake(), vehicle.getModel(),
                        vehicle.getVehicleType(), vehicle.getColor(), vehicle.getPrice());
            }
            System.out.println("-------------------------------------------------------------");
        }
    }

    private void processAddVehicleRequest(Scanner scanner) {
        System.out.print("Enter vehicle details (vin, year, make, model, type, color, odometer, price): ");
        String[] vehicleDetails = scanner.nextLine().split(",");
        int vin = Integer.parseInt(vehicleDetails[0].trim());
        int year = Integer.parseInt(vehicleDetails[1].trim());
        String make = vehicleDetails[2].trim();
        String model = vehicleDetails[3].trim();
        String vehicleType = vehicleDetails[4].trim();
        String color = vehicleDetails[5].trim();
        int odometer = Integer.parseInt(vehicleDetails[6].trim());
        double price = Double.parseDouble(vehicleDetails[7].trim());

        Vehicle vehicle = new Vehicle(vin, year, make, model, vehicleType, color, odometer, price);
        dealership.addVehicle(vehicle);
        System.out.println("Vehicle added successfully.");
        DealershipFileManager.saveDealership(dealership); // Save dealership after adding vehicle
    }

    private void processRemoveVehicleRequest(Scanner scanner) {
        System.out.print("Enter VIN of vehicle to remove: ");
        int vin = scanner.nextInt();
        Vehicle vehicleToRemove = null;
        for (Vehicle vehicle : dealership.getAllVehicles()) {
            if (vehicle.getVin() == vin) {
                vehicleToRemove = vehicle;
                break;
            }
        }
        if (vehicleToRemove != null) {
            dealership.removeVehicle(vehicleToRemove);
            System.out.println("Vehicle removed successfully.");
            DealershipFileManager.saveDealership(dealership); // Save dealership after removing vehicle
        } else {
            System.out.println("Vehicle with VIN " + vin + " not found.");
        }
    }
    private void processAllVehiclesRequest() {
        List<Vehicle> allVehicles = dealership.getAllVehicles();
        displayVehicles(allVehicles);
    }

    private void processGetByYearRequest(Scanner scanner) {
        System.out.print("Enter minimum year: ");
        int minYear = scanner.nextInt();
        System.out.print("Enter maximum year: ");
        int maxYear = scanner.nextInt();
        List<Vehicle> vehicles = dealership.getVehiclesByYear(minYear, maxYear);
        displayVehicles(vehicles);
    }

    private void processGetByColorRequest(Scanner scanner) {
        System.out.print("Enter color: ");
        String color = scanner.nextLine();
        List<Vehicle> vehicles = dealership.getVehiclesByColor(color);
        displayVehicles(vehicles);
    }

    private void processGetByPriceRequest(Scanner scanner) {
        System.out.print("Enter minimum price: ");
        int minPrice = scanner.nextInt();
        System.out.print("Enter maximum price: ");
        int maxPrice = scanner.nextInt();
        List<Vehicle> vehicles = dealership.getVehiclesByPrice(minPrice,maxPrice);
        displayVehicles(vehicles);
    }

    private void processGetByMakeModelRequest(Scanner scanner) {
        System.out.print("Enter Make: ");
        String make = scanner.nextLine();
        System.out.print("Enter Model: ");
        String model = scanner.nextLine();
        List<Vehicle> vehicles = dealership.getVehiclesByMakeModel(make, model);
        displayVehicles(vehicles);
    }

    private void processGetByMileageRequest(Scanner scanner) {
        System.out.print("Enter minimum mileage: ");
        int minMileage = scanner.nextInt();
        System.out.print("Enter maximum mileage: ");
        int maxMileage = scanner.nextInt();
        List<Vehicle> vehicles = dealership.getVehiclesByMileage(minMileage, maxMileage);
        displayVehicles(vehicles);
    }

    private void processGetByVehicleTypeRequest(Scanner scanner) {
        System.out.print("Enter vehicle type: ");
        String vehicleType = scanner.nextLine();
        List<Vehicle> vehicles = dealership.getVehiclesByType(vehicleType);
        displayVehicles(vehicles);
    }
}
