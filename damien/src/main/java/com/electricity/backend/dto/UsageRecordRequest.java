package com.electricity.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public class UsageRecordRequest {

    @NotNull(message = "Usage date is required")
    private LocalDate usageDate;

    @NotNull(message = "Units consumed is required")
    @Positive(message = "Units consumed must be positive")
    private Double unitsConsumed;

    private String accountNumber; // For admin endpoints

    public UsageRecordRequest() {}

    public UsageRecordRequest(LocalDate usageDate, Double unitsConsumed) {
        this.usageDate = usageDate;
        this.unitsConsumed = unitsConsumed;
    }

    public LocalDate getUsageDate() {
        return usageDate;
    }

    public void setUsageDate(LocalDate usageDate) {
        this.usageDate = usageDate;
    }

    public Double getUnitsConsumed() {
        return unitsConsumed;
    }

    public void setUnitsConsumed(Double unitsConsumed) {
        this.unitsConsumed = unitsConsumed;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}