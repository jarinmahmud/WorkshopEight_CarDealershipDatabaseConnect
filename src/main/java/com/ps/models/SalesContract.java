package com.ps.models;

public class SalesContract extends Contract {
    public static final double SALES_TAX_RATE = 0.05;
    public static final double RECORDING_FEE = 100;
    public static final double PROCESSING_FEE_UNDER_10000 = 295;
    public static final double PROCESSING_FEE_OVER_10000 = 495;
    public static final double INTEREST_RATE_OVER_10000 = 0.0425;
    public static final double INTEREST_RATE_UNDER_10000 = 0.0525;
    public static final int LOAN_TERM_OVER_10000 = 48;
    public static final int LOAN_TERM_UNDER_10000 = 24;

    private int contractId;
    private double price;
    private boolean finance;
    private double totalPrice;
    private double monthlyPayment;

    public SalesContract(String date, String customerName, String customerEmail, String vehicleVin, double price, boolean finance) {
        super(date, customerName, customerEmail, vehicleVin);
        this.price = price;
        this.finance = finance;
        this.totalPrice = calculateTotalPrice();
        this.monthlyPayment = calculateMonthlyPayment();
    }

    public int getContractId() {
        return contractId;
    }

    public void setContractId(int contractId) {
        this.contractId = contractId;
    }

    private double calculateTotalPrice() {
        double salesTaxAmount = price * SALES_TAX_RATE;
        double processingFee = price < 10000 ? PROCESSING_FEE_UNDER_10000 : PROCESSING_FEE_OVER_10000;
        return price + salesTaxAmount + RECORDING_FEE + processingFee;
    }

    private double calculateMonthlyPayment() {
        if (!finance) return 0;
        double principal = totalPrice;
        double rate = price >= 10000 ? INTEREST_RATE_OVER_10000 : INTEREST_RATE_UNDER_10000;
        int term = price >= 10000 ? LOAN_TERM_OVER_10000 : LOAN_TERM_UNDER_10000;
        return (principal * rate) / (1 - Math.pow(1 + rate, -term));
    }

    @Override
    public double getTotalPrice() {
        return totalPrice;
    }

    @Override
    public double getMonthlyPayment() {
        return monthlyPayment;
    }

    public double getPrice() {
        return price;
    }

    public boolean isFinance() {
        return finance;
    }

    public double getProcessingFee() {
        return price < 10000 ? PROCESSING_FEE_UNDER_10000 : PROCESSING_FEE_OVER_10000;
    }

    public double getInterestRate() {
        return price >= 10000 ? INTEREST_RATE_OVER_10000 : INTEREST_RATE_UNDER_10000;
    }

    public int getLoanTerm() {
        return price >= 10000 ? LOAN_TERM_OVER_10000 : LOAN_TERM_UNDER_10000;
    }

    @Override
    public String toString() {
        return "SALE|" + super.toString() + price + "|" + monthlyPayment + "|" + RECORDING_FEE + "|"
                + (price < 10000 ? PROCESSING_FEE_UNDER_10000 : PROCESSING_FEE_OVER_10000) + "|" + totalPrice + "|" + (finance ? "YES" : "NO") + "|" + monthlyPayment;
    }
}
