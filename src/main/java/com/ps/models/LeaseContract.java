package com.ps.models;

public class LeaseContract extends Contract {
    private static final double LEASE_RATE = 0.04;
    private static final int LEASE_TERM = 36;

    private int contractId;
    private double price;
    private double totalPrice;
    private double monthlyPayment;

    public LeaseContract(String date, String customerName, String customerEmail, String vehicleVin, double price) {
        super(date, customerName, customerEmail, vehicleVin);
        this.price = price;
        this.totalPrice = calculateTotalPrice();
        this.monthlyPayment = calculateMonthlyPayment();
    }

    public int getContractId() {
        return contractId;
    }

    public void setContractId(int contractId) {
        this.contractId = contractId;
    }

    public double getPrice() {
        return price;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getMonthlyPayment() {
        return monthlyPayment;
    }

    public void setMonthlyPayment(double monthlyPayment) {
        this.monthlyPayment = monthlyPayment;
    }

    private double calculateTotalPrice() {
        double expectedEndingValue = price * 0.5;
        double leaseFee = price * 0.07;
        return expectedEndingValue + leaseFee;
    }

    private double calculateMonthlyPayment() {
        double principal = getTotalPrice();
        return (principal * LEASE_RATE) / (1 - Math.pow(1 + LEASE_RATE, -LEASE_TERM));
    }

    @Override
    public String toString() {
        return "LEASE|" + super.toString() + price + "|" + (price * 0.5) + "|" + (price * 0.07) + "|" + getTotalPrice() + "|" + getMonthlyPayment();
    }
}
