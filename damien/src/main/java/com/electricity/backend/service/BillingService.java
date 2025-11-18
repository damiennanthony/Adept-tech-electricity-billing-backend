package com.electricity.backend.service;

import org.springframework.stereotype.Service;

@Service
public class BillingService {

    public static class BillingResult {
        private final double billAmount;
        private final String billingTier;
        private final double rate;

        public BillingResult(double billAmount, String billingTier, double rate) {
            this.billAmount = billAmount;
            this.billingTier = billingTier;
            this.rate = rate;
        }

        public double getBillAmount() {
            return billAmount;
        }

        public String getBillingTier() {
            return billingTier;
        }

        public double getRate() {
            return rate;
        }
    }

    public BillingResult calculateBill(double unitsConsumed) {
        if (unitsConsumed < 0) {
            throw new IllegalArgumentException("Units consumed cannot be negative");
        }

        double rate;
        String tier;

        if (unitsConsumed <= 30) {
            rate = 20;
            tier = "0-30 kWh";
        } else if (unitsConsumed <= 60) {
            rate = 30;
            tier = "31-60 kWh";
        } else if (unitsConsumed <= 90) {
            rate = 40;
            tier = "61-90 kWh";
        } else if (unitsConsumed <= 120) {
            rate = 80;
            tier = "91-120 kWh";
        } else {
            rate = 120;
            tier = ">120 kWh";
        }

        double billAmount = unitsConsumed * rate;

        return new BillingResult(billAmount, tier, rate);
    }

    public String getBillingTierForUnits(double unitsConsumed) {
        return calculateBill(unitsConsumed).getBillingTier();
    }

    public double getRateForUnits(double unitsConsumed) {
        return calculateBill(unitsConsumed).getRate();
    }
}