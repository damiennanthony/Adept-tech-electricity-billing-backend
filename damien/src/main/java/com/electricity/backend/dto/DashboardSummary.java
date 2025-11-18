package com.electricity.backend.dto;

public class DashboardSummary {

    private double totalUnitsConsumed;
    private double totalBillAmount;
    private String billingTier;
    private double currentRate;
    private int month;
    private int year;

    public DashboardSummary() {}

    public DashboardSummary(double totalUnitsConsumed, double totalBillAmount, String billingTier, double currentRate, int month, int year) {
        this.totalUnitsConsumed = totalUnitsConsumed;
        this.totalBillAmount = totalBillAmount;
        this.billingTier = billingTier;
        this.currentRate = currentRate;
        this.month = month;
        this.year = year;
    }

    public double getTotalUnitsConsumed() {
        return totalUnitsConsumed;
    }

    public void setTotalUnitsConsumed(double totalUnitsConsumed) {
        this.totalUnitsConsumed = totalUnitsConsumed;
    }

    public double getTotalBillAmount() {
        return totalBillAmount;
    }

    public void setTotalBillAmount(double totalBillAmount) {
        this.totalBillAmount = totalBillAmount;
    }

    public String getBillingTier() {
        return billingTier;
    }

    public void setBillingTier(String billingTier) {
        this.billingTier = billingTier;
    }

    public double getCurrentRate() {
        return currentRate;
    }

    public void setCurrentRate(double currentRate) {
        this.currentRate = currentRate;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}