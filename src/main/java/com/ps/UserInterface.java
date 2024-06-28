package com.ps;

import com.ps.database.*;
import com.ps.models.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class UserInterface {
    private VehicleDao vehicleDao;
    private SalesDao salesDao;
    private LeaseContractDao leaseContractDao;

    // Create DAOs here
    private void init() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/CarDealership", "root", "1234");
            vehicleDao = new VehicleDaoImpl(connection);
            salesDao = new SalesDaoImpl(connection);
            leaseContractDao = new LeaseContractDaoImpl(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    // Home Prompt
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
                    processSellOrLeaseVehicleRequest(scanner);
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
                System.out.printf("| %-10s | %-4d | %-10s | %-10s | %-10s | %-6s | %-8.2f |\n",
                        vehicle.getVin(), vehicle.getYear(), vehicle.getMake(), vehicle.getModel(),
                        vehicle.getVehicleType(), vehicle.getColor(), vehicle.getPrice());
            }
            System.out.println("-------------------------------------------------------------");
        }
    }

    private void processAddVehicleRequest(Scanner scanner) {
        System.out.print("Enter vehicle details (vin, year, make, model, type, color, odometer, price): ");
        String[] vehicleDetails = scanner.nextLine().split(",");
        String vin = vehicleDetails[0].trim();
        int year = Integer.parseInt(vehicleDetails[1].trim());
        String make = vehicleDetails[2].trim();
        String model = vehicleDetails[3].trim();
        String vehicleType = vehicleDetails[4].trim();
        String color = vehicleDetails[5].trim();
        int odometer = Integer.parseInt(vehicleDetails[6].trim());
        double price = Double.parseDouble(vehicleDetails[7].trim());

        Vehicle vehicle = new Vehicle(vin, year, make, model, vehicleType, color, odometer, price);
        vehicleDao.addVehicle(vehicle);
        System.out.println("Vehicle added successfully.");
    }

    private void processRemoveVehicleRequest(Scanner scanner) {
        System.out.print("Enter VIN of vehicle to remove: ");
        String vin = scanner.nextLine();
        Vehicle vehicle = vehicleDao.getVehicle(vin);
        if (vehicle != null) {
            vehicleDao.removeVehicle( vin);
            System.out.println("Vehicle removed successfully.");
        } else {
            System.out.println("Vehicle with VIN " + vin + " not found.");
        }
    }

    private void processAllVehiclesRequest() {
        List<Vehicle> allVehicles = vehicleDao.getAllVehicles();
        displayVehicles(allVehicles);
    }

    private void processGetByYearRequest(Scanner scanner) {
        System.out.print("Enter minimum year: ");
        int minYear = scanner.nextInt();
        System.out.print("Enter maximum year: ");
        int maxYear = scanner.nextInt();
        List<Vehicle> vehicles = vehicleDao.findByYearRange(minYear, maxYear);
        displayVehicles(vehicles);
    }

    private void processGetByColorRequest(Scanner scanner) {
        System.out.print("Enter color: ");
        String color = scanner.nextLine();
        List<Vehicle> vehicles = vehicleDao.findByColor(color);
        displayVehicles(vehicles);
    }

    private void processGetByPriceRequest(Scanner scanner) {
        System.out.print("Enter minimum price: ");
        double minPrice = scanner.nextDouble();
        System.out.print("Enter maximum price: ");
        double maxPrice = scanner.nextDouble();
        List<Vehicle> vehicles = vehicleDao.findByPriceRange(minPrice, maxPrice);
        displayVehicles(vehicles);
    }

    private void processGetByMakeModelRequest(Scanner scanner) {
        System.out.print("Enter Make: ");
        String make = scanner.nextLine();
        System.out.print("Enter Model: ");
        String model = scanner.nextLine();
        List<Vehicle> vehicles = vehicleDao.findByMakeModel(make, model);
        displayVehicles(vehicles);
    }

    private void processGetByMileageRequest(Scanner scanner) {
        System.out.print("Enter minimum mileage: ");
        int minMileage = scanner.nextInt();
        System.out.print("Enter maximum mileage: ");
        int maxMileage = scanner.nextInt();
        List<Vehicle> vehicles = vehicleDao.findByMileageRange(minMileage, maxMileage);
        displayVehicles(vehicles);
    }

    private void processGetByVehicleTypeRequest(Scanner scanner) {
        System.out.print("Enter vehicle type: ");
        String vehicleType = scanner.nextLine();
        List<Vehicle> vehicles = vehicleDao.findByType(vehicleType);
        displayVehicles(vehicles);
    }

    private void processSellOrLeaseVehicleRequest(Scanner scanner) {
        System.out.print("Enter date (yyyy-MM-dd): ");
        String dateStr = scanner.nextLine();
        LocalDate date = null;
        try {
            date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please enter the date in the format yyyy-MM-dd.");
            return;
        }

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

            contract = new SalesContract(dateStr, customerName, customerEmail, vehicleVin, price, finance);
            salesDao.addSalesContract((SalesContract) contract);
        } else if ("lease".equalsIgnoreCase(contractType)) {
            if (date.isBefore(LocalDate.now().minusYears(3))) {
                System.out.println("You can't lease a vehicle over 3 years old.");
                return;
            }

            contract = new LeaseContract(dateStr, customerName, customerEmail, vehicleVin, price);
            leaseContractDao.addLeaseContract((LeaseContract) contract);
        } else {
            System.out.println("Invalid contract type.");
            return;
        }

        vehicleDao.removeVehicle(vehicleVin);
        System.out.println("Contract saved and vehicle removed from inventory.");
    }
}
