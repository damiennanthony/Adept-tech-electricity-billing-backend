package com.electricity.backend.controller;

import com.electricity.backend.dto.BillingTierData;
import com.electricity.backend.dto.DailyChartData;
import com.electricity.backend.dto.MonthlyChartData;
import com.electricity.backend.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analytics")
@Tag(name = "Analytics", description = "Analytics and chart data endpoints")
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin(origins = "*")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @Autowired
    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/charts/daily")
    @Operation(summary = "Get daily chart data", description = "Get bar chart data for last 7 days usage")
    public ResponseEntity<List<DailyChartData>> getDailyChartData() {
        try {
            List<DailyChartData> data = analyticsService.getLast7DaysChart();
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/charts/monthly")
    @Operation(summary = "Get monthly chart data", description = "Get line chart data for last 6 months trend")
    public ResponseEntity<List<MonthlyChartData>> getMonthlyChartData() {
        try {
            List<MonthlyChartData> data = analyticsService.getLast6MonthsChart();
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/charts/billing-tiers")
    @Operation(summary = "Get billing tier chart data", description = "Get pie chart data for current month billing tier breakdown")
    public ResponseEntity<List<BillingTierData>> getBillingTierChartData() {
        try {
            List<BillingTierData> data = analyticsService.getCurrentMonthBillingTiers();
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}