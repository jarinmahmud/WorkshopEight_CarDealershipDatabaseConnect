package com.ps;

public abstract class Contract {
    private String date;
    private String customerName;
    private String customerEmail;
    private String vehicleVin;
    private double totalPrice;
    private double monthlyPayment;

    public Contract(String date, String customerName, String customerEmail, String vehicleVin) {
        this.date = date;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.vehicleVin = vehicleVin;
    }

    public String getDate() {
        return date;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public String getVehicleVin() {
        return vehicleVin;
    }

    public abstract double getTotalPrice();

    public abstract double getMonthlyPayment();

    @Override
    public String toString() {
        return date + "|" + customerName + "|" + customerEmail + "|" + vehicleVin + "|";
    }
}
