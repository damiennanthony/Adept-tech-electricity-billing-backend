package com.electricity.backend.dto;

import java.time.LocalDate;

public class DailyChartData {

    private LocalDate date;
    private double unitsConsumed;
    private double billAmount;
    private String billingTier;

    public DailyChartData() {}

    public DailyChartData(LocalDate date, double unitsConsumed, double billAmount, String billingTier) {
        this.date = date;
        this.unitsConsumed = unitsConsumed;
        this.billAmount = billAmount;
        this.billingTier = billingTier;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getUnitsConsumed() {
        return unitsConsumed;
    }

    public void setUnitsConsumed(double unitsConsumed) {
        this.unitsConsumed = unitsConsumed;
    }

    public double getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(double billAmount) {
        this.billAmount = billAmount;
    }

    public String getBillingTier() {
        return billingTier;
    }

    public void setBillingTier(String billingTier) {
        this.billingTier = billingTier;
    }
}