package com.electricity.backend.service;

import com.electricity.backend.dto.DashboardSummary;
import com.electricity.backend.dto.UsageRecordRequest;
import com.electricity.backend.dto.UsageRecordResponse;
import com.electricity.backend.entity.UsageRecord;
import com.electricity.backend.entity.User;
import com.electricity.backend.repository.UsageRecordRepository;
import com.electricity.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsageService {

    private final UsageRecordRepository usageRecordRepository;
    private final UserRepository userRepository;
    private final BillingService billingService;

    @Autowired
    public UsageService(
            UsageRecordRepository usageRecordRepository,
            UserRepository userRepository,
            BillingService billingService
    ) {
        this.usageRecordRepository = usageRecordRepository;
        this.userRepository = userRepository;
        this.billingService = billingService;
    }

    public UsageRecordResponse addUsageRecord(UsageRecordRequest request) {
        User currentUser = getCurrentUser();

        UsageRecord usageRecord = new UsageRecord();
        usageRecord.setUser(currentUser);
        usageRecord.setUsageDate(request.getUsageDate());
        usageRecord.setUnitsConsumed(request.getUnitsConsumed());

        BillingService.BillingResult billing = billingService.calculateBill(request.getUnitsConsumed());
        usageRecord.setBillAmount(billing.getBillAmount());
        usageRecord.setBillingTier(billing.getBillingTier());

        UsageRecord savedRecord = usageRecordRepository.save(usageRecord);

        return mapToResponse(savedRecord);
    }

    public DashboardSummary getCurrentMonthSummary() {
        User currentUser = getCurrentUser();
        LocalDate now = LocalDate.now();
        int currentYear = now.getYear();
        int currentMonth = now.getMonthValue();

        Double totalUnits = usageRecordRepository.findTotalUsageByUserAndYearAndMonth(
                currentUser, currentYear, currentMonth);

        if (totalUnits == null) {
            totalUnits = 0.0;
        }

        BillingService.BillingResult billing = billingService.calculateBill(totalUnits);

        return new DashboardSummary(
                totalUnits,
                billing.getBillAmount(),
                billing.getBillingTier(),
                billing.getRate(),
                currentMonth,
                currentYear
        );
    }

    public List<UsageRecordResponse> getDailyUsage(int days) {
        User currentUser = getCurrentUser();
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days - 1);

        List<UsageRecord> records = usageRecordRepository.findByUserAndUsageDateBetweenOrderByUsageDateDesc(
                currentUser, startDate, endDate);

        return records.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<UsageRecordResponse> getMonthlyUsage(int year, int month) {
        User currentUser = getCurrentUser();

        List<UsageRecord> records = usageRecordRepository.findByUserAndYearAndMonth(
                currentUser, year, month);

        return records.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public DashboardSummary getMonthSummary(int year, int month) {
        User currentUser = getCurrentUser();

        Double totalUnits = usageRecordRepository.findTotalUsageByUserAndYearAndMonth(
                currentUser, year, month);

        if (totalUnits == null) {
            totalUnits = 0.0;
        }

        BillingService.BillingResult billing = billingService.calculateBill(totalUnits);

        return new DashboardSummary(
                totalUnits,
                billing.getBillAmount(),
                billing.getBillingTier(),
                billing.getRate(),
                month,
                year
        );
    }

    public List<UsageRecordResponse> getLast7DaysUsage() {
        return getDailyUsage(7);
    }

    public UsageRecordResponse addUsageRecordForUser(String accountNumber, UsageRecordRequest request) {
        User user = userRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UsageRecord usageRecord = new UsageRecord();
        usageRecord.setUser(user);
        usageRecord.setUsageDate(request.getUsageDate());
        usageRecord.setUnitsConsumed(request.getUnitsConsumed());

        BillingService.BillingResult billing = billingService.calculateBill(request.getUnitsConsumed());
        usageRecord.setBillAmount(billing.getBillAmount());
        usageRecord.setBillingTier(billing.getBillingTier());

        UsageRecord savedRecord = usageRecordRepository.save(usageRecord);

        return mapToResponse(savedRecord);
    }

    private User getCurrentUser() {
        String accountNumber = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private UsageRecordResponse mapToResponse(UsageRecord record) {
        return new UsageRecordResponse(
                record.getId(),
                record.getUsageDate(),
                record.getUnitsConsumed(),
                record.getBillAmount(),
                record.getBillingTier(),
                record.getCreatedAt(),
                record.getUpdatedAt()
        );
    }
}