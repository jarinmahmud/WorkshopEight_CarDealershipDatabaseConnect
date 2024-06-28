package com.ps;

import com.ps.database.VehicleDaoImpl;
import com.ps.models.Vehicle;
import org.junit.jupiter.api.*;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class VehicleDaoImplTest {

    private static Connection connection;
    private VehicleDaoImpl vehicleDao;

    @BeforeAll
    static void init() throws SQLException {
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE Vehicle (" +
                    "vin VARCHAR(20) PRIMARY KEY, " +
                    "year INT, " +
                    "make VARCHAR(50), " +
                    "model VARCHAR(50), " +
                    "vehicleType VARCHAR(50), " +
                    "color VARCHAR(20), " +
                    "odometer INT, " +
                    "price DOUBLE)");

            stmt.execute("CREATE TABLE Contract (" +
                    "contractId INT AUTO_INCREMENT PRIMARY KEY, " +
                    "date DATE, " +
                    "customerName VARCHAR(100), " +
                    "customerEmail VARCHAR(100), " +
                    "vehicleVin VARCHAR(20), " +
                    "type VARCHAR(10), " +
                    "FOREIGN KEY (vehicleVin) REFERENCES Vehicle(vin))");

            stmt.execute("CREATE TABLE SalesContract (" +
                    "contractId INT PRIMARY KEY, " +
                    "price DOUBLE, " +
                    "finance BOOLEAN, " +
                    "totalPrice DOUBLE, " +
                    "monthlyPayment DOUBLE, " +
                    "salesTaxRate DOUBLE, " +
                    "recordingFee DOUBLE, " +
                    "processingFee DOUBLE, " +
                    "interestRate DOUBLE, " +
                    "loanTerm INT, " +
                    "FOREIGN KEY (contractId) REFERENCES Contract(contractId))");

            stmt.execute("CREATE TABLE LeaseContract (" +
                    "contractId INT PRIMARY KEY, " +
                    "price DOUBLE, " +
                    "totalPrice DOUBLE, " +
                    "monthlyPayment DOUBLE, " +
                    "FOREIGN KEY (contractId) REFERENCES Contract(contractId))");
        }
    }

    @BeforeEach
    void setUp() {
        vehicleDao = new VehicleDaoImpl(connection);
    }

    @Test
    void removeVehicleWithSalesContract() throws SQLException {
        insertSampleVehicle();
        insertSampleContract("SALE");
        insertSampleSalesContract();

        // Remove vehicle
        vehicleDao.removeVehicle("1HGCM82633A123456");

        // Verify the vehicle and contracts are removed
        verifyVehicleAndContractsRemoved();
    }

    @Test
    void removeVehicleWithLeaseContract() throws SQLException {
        insertSampleVehicle();
        insertSampleContract("LEASE");
        insertSampleLeaseContract();

        // Remove vehicle
        vehicleDao.removeVehicle("1HGCM82633A123456");

        // Verify the vehicle and contracts are removed
        verifyVehicleAndContractsRemoved();
    }

    private void insertSampleVehicle() throws SQLException {
        try (PreparedStatement vehicleStmt = connection.prepareStatement("INSERT INTO Vehicle (vin, year, make, model, vehicleType, color, odometer, price) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
            vehicleStmt.setString(1, "1HGCM82633A123456");
            vehicleStmt.setInt(2, 2023);
            vehicleStmt.setString(3, "Honda");
            vehicleStmt.setString(4, "Accord");
            vehicleStmt.setString(5, "Sedan");
            vehicleStmt.setString(6, "Blue");
            vehicleStmt.setInt(7, 5000);
            vehicleStmt.setDouble(8, 25000);
            vehicleStmt.executeUpdate();
        }
    }

    private int insertSampleContract(String type) throws SQLException {
        try (PreparedStatement contractStmt = connection.prepareStatement("INSERT INTO Contract (date, customerName, customerEmail, vehicleVin, type) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            contractStmt.setDate(1, Date.valueOf("2024-06-27"));
            contractStmt.setString(2, "John Doe");
            contractStmt.setString(3, "johndoe@example.com");
            contractStmt.setString(4, "1HGCM82633A123456");
            contractStmt.setString(5, type);
            contractStmt.executeUpdate();

            ResultSet generatedKeys = contractStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            }
        }
        return 0;
    }

    private void insertSampleSalesContract() throws SQLException {
        int contractId = insertSampleContract("SALE");
        try (PreparedStatement salesStmt = connection.prepareStatement("INSERT INTO SalesContract (contractId, price, finance, totalPrice, monthlyPayment, salesTaxRate, recordingFee, processingFee, interestRate, loanTerm) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            salesStmt.setInt(1, contractId);
            salesStmt.setDouble(2, 25000);
            salesStmt.setBoolean(3, true);
            salesStmt.setDouble(4, 25000 * 1.05 + 100 + 495);
            salesStmt.setDouble(5, (25000 * 1.05 + 100 + 495) * 0.0425 / (1 - Math.pow(1 + 0.0425, -48)));
            salesStmt.setDouble(6, 0.05);
            salesStmt.setDouble(7, 100);
            salesStmt.setDouble(8, 495);
            salesStmt.setDouble(9, 0.0425);
            salesStmt.setInt(10, 48);
            salesStmt.executeUpdate();
        }
    }

    private void insertSampleLeaseContract() throws SQLException {
        int contractId = insertSampleContract("LEASE");
        try (PreparedStatement leaseStmt = connection.prepareStatement("INSERT INTO LeaseContract (contractId, price, totalPrice, monthlyPayment) VALUES (?, ?, ?, ?)")) {
            leaseStmt.setInt(1, contractId);
            leaseStmt.setDouble(2, 25000);
            leaseStmt.setDouble(3, 25000 * 0.5 + 25000 * 0.07);
            leaseStmt.setDouble(4, (25000 * 0.5 + 25000 * 0.07) * 0.04 / (1 - Math.pow(1 + 0.04, -36)));
            leaseStmt.executeUpdate();
        }
    }

    private void verifyVehicleAndContractsRemoved() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM Vehicle WHERE vin = '1HGCM82633A123456'");
            assertFalse(rs.next());

            rs = stmt.executeQuery("SELECT * FROM Contract WHERE vehicleVin = '1HGCM82633A123456'");
            assertFalse(rs.next());

            rs = stmt.executeQuery("SELECT * FROM SalesContract WHERE contractId = 1");
            assertFalse(rs.next());

            rs = stmt.executeQuery("SELECT * FROM LeaseContract WHERE contractId = 1");
            assertFalse(rs.next());
        }
    }
}
