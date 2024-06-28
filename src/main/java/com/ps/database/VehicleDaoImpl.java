package com.ps.database;

import com.ps.models.Vehicle;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleDaoImpl implements VehicleDao {
    private Connection connection;

    public VehicleDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Vehicle> findByPriceRange(double minPrice, double maxPrice) {
        List<Vehicle> vehicles = new ArrayList<>();
        String query = "SELECT * FROM Vehicle WHERE price BETWEEN ? AND ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDouble(1, minPrice);
            stmt.setDouble(2, maxPrice);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                vehicles.add(mapRowToVehicle(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicles;
    }

    @Override
    public List<Vehicle> findByMakeModel(String make, String model) {
        List<Vehicle> vehicles = new ArrayList<>();
        String query = "SELECT * FROM Vehicle WHERE make = ? AND model = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, make);
            stmt.setString(2, model);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                vehicles.add(mapRowToVehicle(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicles;
    }

    @Override
    public List<Vehicle> findByYearRange(int minYear, int maxYear) {
        List<Vehicle> vehicles = new ArrayList<>();
        String query = "SELECT * FROM Vehicle WHERE year BETWEEN ? AND ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, minYear);
            stmt.setInt(2, maxYear);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                vehicles.add(mapRowToVehicle(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicles;
    }

    @Override
    public List<Vehicle> findByColor(String color) {
        List<Vehicle> vehicles = new ArrayList<>();
        String query = "SELECT * FROM Vehicle WHERE color = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, color);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                vehicles.add(mapRowToVehicle(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicles;
    }

    @Override
    public List<Vehicle> findByMileageRange(int minMileage, int maxMileage) {
        List<Vehicle> vehicles = new ArrayList<>();
        String query = "SELECT * FROM Vehicle WHERE odometer BETWEEN ? AND ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, minMileage);
            stmt.setInt(2, maxMileage);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                vehicles.add(mapRowToVehicle(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicles;
    }

    @Override
    public List<Vehicle> findByType(String type) {
        List<Vehicle> vehicles = new ArrayList<>();
        String query = "SELECT * FROM Vehicle WHERE vehicleType = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, type);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                vehicles.add(mapRowToVehicle(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicles;
    }

    @Override
    public void addVehicle(Vehicle vehicle) {
        String query = "INSERT INTO Vehicle (vin, year, make, model, vehicleType, color, odometer, price, dealershipId) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, vehicle.getVin());
            stmt.setInt(2, vehicle.getYear());
            stmt.setString(3, vehicle.getMake());
            stmt.setString(4, vehicle.getModel());
            stmt.setString(5, vehicle.getVehicleType());
            stmt.setString(6, vehicle.getColor());
            stmt.setInt(7, vehicle.getOdometer());
            stmt.setDouble(8, vehicle.getPrice());
            stmt.setInt(9, 1); // assuming dealershipId is 1 for simplicity
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeVehicle(String vin) {
        String deleteSalesContractQuery = "DELETE FROM SalesContract WHERE contractId IN (SELECT contractId FROM Contract WHERE vehicleVin = ?)";
        String deleteLeaseContractQuery = "DELETE FROM LeaseContract WHERE contractId IN (SELECT contractId FROM Contract WHERE vehicleVin = ?)";
        String deleteContractQuery = "DELETE FROM Contract WHERE vehicleVin = ?";
        String deleteVehicleQuery = "DELETE FROM Vehicle WHERE vin = ?";
        try (PreparedStatement salesContractStmt = connection.prepareStatement(deleteSalesContractQuery);
             PreparedStatement leaseContractStmt = connection.prepareStatement(deleteLeaseContractQuery);
             PreparedStatement contractStmt = connection.prepareStatement(deleteContractQuery);
             PreparedStatement vehicleStmt = connection.prepareStatement(deleteVehicleQuery)) {

            connection.setAutoCommit(false);

            // Delete from SalesContract table
            salesContractStmt.setString(1, vin);
            salesContractStmt.executeUpdate();

            // Delete from LeaseContract table
            leaseContractStmt.setString(1, vin);
            leaseContractStmt.executeUpdate();

            // Delete from Contract table
            contractStmt.setString(1, vin);
            contractStmt.executeUpdate();

            // Delete from Vehicle table
            vehicleStmt.setString(1, vin);
            vehicleStmt.executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    @Override
    public Vehicle getVehicle(String vin) {
        String query = "SELECT * FROM Vehicle WHERE vin = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, vin);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapRowToVehicle(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Vehicle> getAllVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        String query = "SELECT * FROM Vehicle";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                vehicles.add(mapRowToVehicle(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicles;
    }

    private Vehicle mapRowToVehicle(ResultSet rs) throws SQLException {
        return new Vehicle(
                rs.getString("vin"),
                rs.getInt("year"),
                rs.getString("make"),
                rs.getString("model"),
                rs.getString("vehicleType"),
                rs.getString("color"),
                rs.getInt("odometer"),
                rs.getDouble("price")
        );
    }
}
