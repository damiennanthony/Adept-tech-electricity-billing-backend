package com.electricity.backend.dto;

public class BillingTierData {

    private String tierName;
    private double percentage;
    private double amount;
    private String color;

    public BillingTierData() {}

    public BillingTierData(String tierName, double percentage, double amount, String color) {
        this.tierName = tierName;
        this.percentage = percentage;
        this.amount = amount;
        this.color = color;
    }

    public String getTierName() {
        return tierName;
    }

    public void setTierName(String tierName) {
        this.tierName = tierName;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}