package com.ps;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleDaoImpl implements VehicleDao {
    @Override
    public List<Vehicle> findByPriceRange(double minPrice, double maxPrice) {
        List<Vehicle> vehicles = new ArrayList<>();
        String query = "SELECT * FROM vehicles WHERE price BETWEEN ? AND ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setDouble(1, minPrice);
            ps.setDouble(2, maxPrice);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                vehicles.add(mapResultSetToVehicle(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicles;
    }

    @Override
    public List<Vehicle> findByMakeModel(String make, String model) {
        return List.of();
    }

    @Override
    public List<Vehicle> findByYearRange(int minYear, int maxYear) {
        return List.of();
    }

    @Override
    public List<Vehicle> findByColor(String color) {
        return List.of();
    }

    @Override
    public List<Vehicle> findByMileageRange(int minMileage, int maxMileage) {
        return List.of();
    }

    @Override
    public List<Vehicle> findByType(String type) {
        return List.of();
    }

    // Other search methods follow the same pattern...

    @Override
    public void addVehicle(Vehicle vehicle) {
        String query = "INSERT INTO vehicles (make, model, year, color, mileage, type, price) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, vehicle.getMake());
            ps.setString(2, vehicle.getModel());
            ps.setInt(3, vehicle.getYear());
            ps.setString(4, vehicle.getColor());
            ps.setInt(5, vehicle.getMileage());
            ps.setString(6, vehicle.getType());
            ps.setDouble(7, vehicle.getPrice());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeVehicle(int id) {
        String query = "DELETE FROM vehicles WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Vehicle mapResultSetToVehicle(ResultSet rs) throws SQLException {
        return new Vehicle(
                rs.getInt("id"),
                rs.getString("make"),
                rs.getString("model"),
                rs.getInt("year"),
                rs.getString("color"),
                rs.getInt("mileage"),
                rs.getString("type"),
                rs.getDouble("price")
        );
    }
}

