package com.ps;

public class SalesContract extends Contract {
    private static final double SALES_TAX_RATE = 0.05;
    private static final double RECORDING_FEE = 100;
    private static final double PROCESSING_FEE_UNDER_10000 = 295;
    private static final double PROCESSING_FEE_OVER_10000 = 495;
    private static final double INTEREST_RATE_OVER_10000 = 0.0425;
    private static final double INTEREST_RATE_UNDER_10000 = 0.0525;
    private static final int LOAN_TERM_OVER_10000 = 48;
    private static final int LOAN_TERM_UNDER_10000 = 24;

    private double price;
    private boolean finance;

    public SalesContract(String date, String customerName, String customerEmail, String vehicleVin, double price, boolean finance) {
        super(date, customerName, customerEmail, vehicleVin);
        this.price = price;
        this.finance = finance;
    }

    @Override
    public double getTotalPrice() {
        double salesTaxAmount = price * SALES_TAX_RATE;
        double processingFee = price < 10000 ? PROCESSING_FEE_UNDER_10000 : PROCESSING_FEE_OVER_10000;
        return price + salesTaxAmount + RECORDING_FEE + processingFee;
    }

    @Override
    public double getMonthlyPayment() {
        if (!finance) return 0;
        double principal = getTotalPrice();
        double rate = price >= 10000 ? INTEREST_RATE_OVER_10000 : INTEREST_RATE_UNDER_10000;
        int term = price >= 10000 ? LOAN_TERM_OVER_10000 : LOAN_TERM_UNDER_10000;

        return (principal * rate) / (1 - Math.pow(1 + rate, -term));
    }

    @Override
    public String toString() {
        return "SALE|" + super.toString() + price + "|" + getMonthlyPayment() + "|" + RECORDING_FEE + "|"
                + (price < 10000 ? PROCESSING_FEE_UNDER_10000 : PROCESSING_FEE_OVER_10000) + "|" + getTotalPrice() + "|" + (finance ? "YES" : "NO") + "|" + getMonthlyPayment();
    }
}
