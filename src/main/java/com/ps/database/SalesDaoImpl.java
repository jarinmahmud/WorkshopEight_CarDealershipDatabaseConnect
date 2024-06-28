package com.ps.database;

import com.ps.models.SalesContract;

import java.sql.*;

public class SalesDaoImpl implements SalesDao {

    private Connection connection;

    public SalesDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addSalesContract(SalesContract salesContract) {
        String contractQuery = "INSERT INTO Contract (date, customerName, customerEmail, vehicleVin, type) VALUES (?, ?, ?, ?, ?)";
        String salesContractQuery = "INSERT INTO SalesContract (contractId, price, finance, totalPrice, monthlyPayment, salesTaxRate, recordingFee, processingFee, interestRate, loanTerm) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement contractStmt = connection.prepareStatement(contractQuery, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement salesStmt = connection.prepareStatement(salesContractQuery)) {

                // Insert into Contract table
                contractStmt.setString(1, salesContract.getDate());
                contractStmt.setString(2, salesContract.getCustomerName());
                contractStmt.setString(3, salesContract.getCustomerEmail());
                contractStmt.setString(4, salesContract.getVehicleVin());
                contractStmt.setString(5, "SALE");
                contractStmt.executeUpdate();

                ResultSet generatedKeys = contractStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int contractId = generatedKeys.getInt(1);

                    // Insert into SalesContract table
                    salesStmt.setInt(1, contractId);
                    salesStmt.setDouble(2, salesContract.getPrice());
                    salesStmt.setBoolean(3, salesContract.isFinance());
                    salesStmt.setDouble(4, salesContract.getTotalPrice());
                    salesStmt.setDouble(5, salesContract.getMonthlyPayment());
                    salesStmt.setDouble(6, SalesContract.SALES_TAX_RATE);
                    salesStmt.setDouble(7, SalesContract.RECORDING_FEE);
                    salesStmt.setDouble(8, salesContract.getProcessingFee());
                    salesStmt.setDouble(9, salesContract.getInterestRate());
                    salesStmt.setInt(10, salesContract.getLoanTerm());
                    salesStmt.executeUpdate();
                }

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public SalesContract getSalesContract(int contractId) {
        String query = "SELECT c.date, c.customerName, c.customerEmail, c.vehicleVin, s.price, s.finance " +
                "FROM Contract c " +
                "JOIN SalesContract s ON c.contractId = s.contractId " +
                "WHERE c.contractId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, contractId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new SalesContract(
                            rs.getString("date"),
                            rs.getString("customerName"),
                            rs.getString("customerEmail"),
                            rs.getString("vehicleVin"),
                            rs.getDouble("price"),
                            rs.getBoolean("finance")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void updateSalesContract(SalesContract salesContract) {
        String query = "UPDATE SalesContract SET price = ?, finance = ?, totalPrice = ?, monthlyPayment = ?, salesTaxRate = ?, recordingFee = ?, processingFee = ?, interestRate = ?, loanTerm = ? WHERE contractId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDouble(1, salesContract.getPrice());
            stmt.setBoolean(2, salesContract.isFinance());
            stmt.setDouble(3, salesContract.getTotalPrice());
            stmt.setDouble(4, salesContract.getMonthlyPayment());
            stmt.setDouble(5, SalesContract.SALES_TAX_RATE);
            stmt.setDouble(6, SalesContract.RECORDING_FEE);
            stmt.setDouble(7, salesContract.getProcessingFee());
            stmt.setDouble(8, salesContract.getInterestRate());
            stmt.setInt(9, salesContract.getLoanTerm());
            stmt.setInt(10, salesContract.getContractId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteSalesContract(int contractId) {
        String query = "DELETE FROM SalesContract WHERE contractId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, contractId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
