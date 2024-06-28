package com.ps.database;
import com.ps.models.Vehicle;
import java.util.List;

public interface VehicleDao {
    List<Vehicle> findByPriceRange(double minPrice, double maxPrice);
    List<Vehicle> findByMakeModel(String make, String model);
    List<Vehicle> findByYearRange(int minYear, int maxYear);
    List<Vehicle> findByColor(String color);
    List<Vehicle> findByMileageRange(int minMileage, int maxMileage);
    List<Vehicle> findByType(String type);
    void addVehicle(Vehicle vehicle);
    void removeVehicle(String vin);
    Vehicle getVehicle(String vin); // Added method
    List<Vehicle> getAllVehicles(); // Added method
}
