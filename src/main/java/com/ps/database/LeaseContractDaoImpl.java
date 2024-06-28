package com.ps.database;

import com.ps.models.LeaseContract;

import java.sql.*;

public class LeaseContractDaoImpl implements LeaseContractDao {

    private Connection connection;

    public LeaseContractDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addLeaseContract(LeaseContract leaseContract) {
        String contractQuery = "INSERT INTO Contract (date, customerName, customerEmail, vehicleVin, type) VALUES (?, ?, ?, ?, ?)";
        String leaseContractQuery = "INSERT INTO LeaseContract (contractId, price, totalPrice, monthlyPayment) VALUES (?, ?, ?, ?)";

        try {

            try (PreparedStatement contractStmt = connection.prepareStatement(contractQuery, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement leaseStmt = connection.prepareStatement(leaseContractQuery)) {

                // Insert into Contract table
                contractStmt.setString(1, leaseContract.getDate());
                contractStmt.setString(2, leaseContract.getCustomerName());
                contractStmt.setString(3, leaseContract.getCustomerEmail());
                contractStmt.setString(4, leaseContract.getVehicleVin());
                contractStmt.setString(5, "LEASE");
                contractStmt.executeUpdate();

                ResultSet generatedKeys = contractStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int contractId = generatedKeys.getInt(1);
                    leaseContract.setContractId(contractId);

                    // Insert into LeaseContract table
                    leaseStmt.setInt(1, contractId);
                    leaseStmt.setDouble(2, leaseContract.getPrice());
                    leaseStmt.setDouble(3, leaseContract.getTotalPrice());
                    leaseStmt.setDouble(4, leaseContract.getMonthlyPayment());
                    leaseStmt.executeUpdate();
                }


            } catch (SQLException e) {

                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public LeaseContract getLeaseContract(int contractId) {
        String query = "SELECT c.date, c.customerName, c.customerEmail, c.vehicleVin, l.price " +
                "FROM Contract c " +
                "JOIN LeaseContract l ON c.contractId = l.contractId " +
                "WHERE c.contractId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, contractId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    LeaseContract leaseContract = new LeaseContract(
                            rs.getString("date"),
                            rs.getString("customerName"),
                            rs.getString("customerEmail"),
                            rs.getString("vehicleVin"),
                            rs.getDouble("price")
                    );
                    leaseContract.setContractId(contractId);
                    return leaseContract;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void updateLeaseContract(LeaseContract leaseContract) {
        String contractQuery = "UPDATE Contract SET date = ?, customerName = ?, customerEmail = ?, vehicleVin = ? WHERE contractId = ?";
        String leaseContractQuery = "UPDATE LeaseContract SET price = ?, totalPrice = ?, monthlyPayment = ? WHERE contractId = ?";

        try {

            try (PreparedStatement contractStmt = connection.prepareStatement(contractQuery);
                 PreparedStatement leaseStmt = connection.prepareStatement(leaseContractQuery)) {

                // Update Contract table
                contractStmt.setString(1, leaseContract.getDate());
                contractStmt.setString(2, leaseContract.getCustomerName());
                contractStmt.setString(3, leaseContract.getCustomerEmail());
                contractStmt.setString(4, leaseContract.getVehicleVin());
                contractStmt.setInt(5, leaseContract.getContractId());
                contractStmt.executeUpdate();

                // Update LeaseContract table
                leaseStmt.setDouble(1, leaseContract.getPrice());
                leaseStmt.setDouble(2, leaseContract.getTotalPrice());
                leaseStmt.setDouble(3, leaseContract.getMonthlyPayment());
                leaseStmt.setInt(4, leaseContract.getContractId());
                leaseStmt.executeUpdate();


            } catch (SQLException e) {
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteLeaseContract(int contractId) {
        String query = "DELETE FROM LeaseContract WHERE contractId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, contractId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
