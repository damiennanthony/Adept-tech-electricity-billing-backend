package com.electricity.backend.dto;

public class MonthlyChartData {

    private int year;
    private int month;
    private String monthName;
    private double totalUnitsConsumed;
    private double totalBillAmount;
    private String billingTier;

    public MonthlyChartData() {}

    public MonthlyChartData(int year, int month, String monthName, double totalUnitsConsumed, double totalBillAmount, String billingTier) {
        this.year = year;
        this.month = month;
        this.monthName = monthName;
        this.totalUnitsConsumed = totalUnitsConsumed;
        this.totalBillAmount = totalBillAmount;
        this.billingTier = billingTier;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
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
}