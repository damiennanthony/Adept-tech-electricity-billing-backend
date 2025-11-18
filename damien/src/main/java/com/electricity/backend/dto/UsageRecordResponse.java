package com.electricity.backend.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class UsageRecordResponse {

    private Long id;
    private LocalDate usageDate;
    private Double unitsConsumed;
    private Double billAmount;
    private String billingTier;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UsageRecordResponse() {}

    public UsageRecordResponse(Long id, LocalDate usageDate, Double unitsConsumed, Double billAmount, String billingTier, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.usageDate = usageDate;
        this.unitsConsumed = unitsConsumed;
        this.billAmount = billAmount;
        this.billingTier = billingTier;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Double getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(Double billAmount) {
        this.billAmount = billAmount;
    }

    public String getBillingTier() {
        return billingTier;
    }

    public void setBillingTier(String billingTier) {
        this.billingTier = billingTier;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}