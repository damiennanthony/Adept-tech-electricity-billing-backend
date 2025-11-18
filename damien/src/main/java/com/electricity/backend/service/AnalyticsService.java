package com.electricity.backend.service;

import com.electricity.backend.dto.BillingTierData;
import com.electricity.backend.dto.DailyChartData;
import com.electricity.backend.dto.MonthlyChartData;
import com.electricity.backend.entity.User;
import com.electricity.backend.repository.UsageRecordRepository;
import com.electricity.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    private final UsageRecordRepository usageRecordRepository;
    private final UserRepository userRepository;
    private final BillingService billingService;

    @Autowired
    public AnalyticsService(
            UsageRecordRepository usageRecordRepository,
            UserRepository userRepository,
            BillingService billingService
    ) {
        this.usageRecordRepository = usageRecordRepository;
        this.userRepository = userRepository;
        this.billingService = billingService;
    }

    public List<DailyChartData> getLast7DaysChart() {
        User currentUser = getCurrentUser();
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(6);

        List<DailyChartData> chartData = new ArrayList<>();

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            Double totalUnits = usageRecordRepository.findTotalUsageSince(currentUser, date);
            if (totalUnits == null) totalUnits = 0.0;

            BillingService.BillingResult billing = billingService.calculateBill(totalUnits);

            chartData.add(new DailyChartData(
                    date,
                    totalUnits,
                    billing.getBillAmount(),
                    billing.getBillingTier()
            ));
        }

        return chartData;
    }

    public List<MonthlyChartData> getLast6MonthsChart() {
        User currentUser = getCurrentUser();
        List<MonthlyChartData> chartData = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();

        for (int i = 5; i >= 0; i--) {
            LocalDate monthDate = currentDate.minusMonths(i);
            int year = monthDate.getYear();
            int month = monthDate.getMonthValue();
            String monthName = monthDate.getMonth().toString().substring(0, 3);

            Double totalUnits = usageRecordRepository.findTotalUsageByUserAndYearAndMonth(
                    currentUser, year, month);
            if (totalUnits == null) totalUnits = 0.0;

            BillingService.BillingResult billing = billingService.calculateBill(totalUnits);

            chartData.add(new MonthlyChartData(
                    year,
                    month,
                    monthName,
                    totalUnits,
                    billing.getBillAmount(),
                    billing.getBillingTier()
            ));
        }

        return chartData;
    }

    public List<BillingTierData> getCurrentMonthBillingTiers() {
        User currentUser = getCurrentUser();
        LocalDate now = LocalDate.now();
        int currentYear = now.getYear();
        int currentMonth = now.getMonthValue();

        Double totalUnits = usageRecordRepository.findTotalUsageByUserAndYearAndMonth(
                currentUser, currentYear, currentMonth);
        if (totalUnits == null) totalUnits = 0.0;

        BillingService.BillingResult billing = billingService.calculateBill(totalUnits);
        double totalBill = billing.getBillAmount();

        List<BillingTierData> tierData = new ArrayList<>();

        // Define tier colors
        Map<String, String> tierColors = Map.of(
                "0-30 kWh", "#4CAF50",    // Green
                "31-60 kWh", "#2196F3",   // Blue
                "61-90 kWh", "#FF9800",   // Orange
                "91-120 kWh", "#F44336",  // Red
                ">120 kWh", "#9C27B0"     // Purple
        );

        // Calculate breakdown by tiers
        if (totalUnits <= 30) {
            tierData.add(new BillingTierData("0-30 kWh", 100.0, totalBill, tierColors.get("0-30 kWh")));
        } else if (totalUnits <= 60) {
            double tier1Amount = 30 * 20;
            double tier2Amount = totalBill - tier1Amount;
            tierData.add(new BillingTierData("0-30 kWh", (tier1Amount / totalBill) * 100, tier1Amount, tierColors.get("0-30 kWh")));
            tierData.add(new BillingTierData("31-60 kWh", (tier2Amount / totalBill) * 100, tier2Amount, tierColors.get("31-60 kWh")));
        } else if (totalUnits <= 90) {
            double tier1Amount = 30 * 20;
            double tier2Amount = 30 * 30;
            double tier3Amount = totalBill - tier1Amount - tier2Amount;
            tierData.add(new BillingTierData("0-30 kWh", (tier1Amount / totalBill) * 100, tier1Amount, tierColors.get("0-30 kWh")));
            tierData.add(new BillingTierData("31-60 kWh", (tier2Amount / totalBill) * 100, tier2Amount, tierColors.get("31-60 kWh")));
            tierData.add(new BillingTierData("61-90 kWh", (tier3Amount / totalBill) * 100, tier3Amount, tierColors.get("61-90 kWh")));
        } else if (totalUnits <= 120) {
            tierData.add(new BillingTierData("91-120 kWh", 100.0, totalBill, tierColors.get("91-120 kWh")));
        } else {
            tierData.add(new BillingTierData(">120 kWh", 100.0, totalBill, tierColors.get(">120 kWh")));
        }

        return tierData.stream()
                .filter(tier -> tier.getAmount() > 0)
                .collect(Collectors.toList());
    }

    private User getCurrentUser() {
        String accountNumber = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}