package com.ps;

public class LeaseContract extends Contract {
    private static final double LEASE_RATE = 0.04;
    private static final int LEASE_TERM = 36;

    private double price;

    public LeaseContract(String date, String customerName, String customerEmail, String vehicleVin, double price) {
        super(date, customerName, customerEmail, vehicleVin);
        this.price = price;
    }

    @Override
    public double getTotalPrice() {
        double expectedEndingValue = price * 0.5;
        double leaseFee = price * 0.07;
        return expectedEndingValue + leaseFee;
    }

    @Override
    public double getMonthlyPayment() {
        double principal = getTotalPrice();
        return (principal * LEASE_RATE) / (1 - Math.pow(1 + LEASE_RATE, -LEASE_TERM));
    }

    @Override
    public String toString() {
        return "LEASE|" + super.toString() + price + "|" + (price * 0.5) + "|" + (price * 0.07) + "|" + getTotalPrice() + "|" + getMonthlyPayment();
    }
}
