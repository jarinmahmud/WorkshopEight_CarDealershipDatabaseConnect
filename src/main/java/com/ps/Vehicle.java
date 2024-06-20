package com.ps;

class Vehicle {
    private int vin;
    private int year;
    private String make;
    private String model;
    private String vehicleType;
    private String color;
    private int odometer;
    private double price;

    //Constructor
    public Vehicle(int vin, String year, String make, int model, String vehicleType, int color, String odometer, double price) {
        this.vin = vin;
        this.year = Integer.parseInt(year);
        this.make = make;
        this.model = String.valueOf(model);
        this.vehicleType = vehicleType;
        this.color = String.valueOf(color);
        this.odometer = Integer.parseInt(odometer);
        this.price = price;
    }

    public Vehicle(int vin, int year, String make, String model, String vehicleType, String color, int odometer, double price) {
    }

    //Getters
    public int getVin() {
        return vin;
    }

    public int getYear() {
        return year;
    }

    public String getMake() {
        return make;
    }


    public String getModel() {
        return model;
    }

    public String getVehicleType() {
        return vehicleType;
    }


    public String getColor() {
        return color;
    }


    public int getOdometer() {
        return odometer;
    }


    public double getPrice() {
        return price;
    }


    public int getMileage() {
        return 0;
    }

    public String getType() {
        return "";
    }
}
