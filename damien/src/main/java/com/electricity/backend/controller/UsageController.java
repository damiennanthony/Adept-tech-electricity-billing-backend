package com.electricity.backend.controller;

import com.electricity.backend.dto.DashboardSummary;
import com.electricity.backend.dto.UsageRecordRequest;
import com.electricity.backend.dto.UsageRecordResponse;
import com.electricity.backend.service.UsageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usage")
@Tag(name = "Usage Tracking", description = "Endpoints for tracking electricity usage")
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin(origins = "*")
public class UsageController {

    private final UsageService usageService;

    @Autowired
    public UsageController(UsageService usageService) {
        this.usageService = usageService;
    }

    @PostMapping
    @Operation(summary = "Add usage record", description = "Add a new electricity usage record")
    public ResponseEntity<UsageRecordResponse> addUsageRecord(@Valid @RequestBody UsageRecordRequest request) {
        try {
            UsageRecordResponse response = usageService.addUsageRecord(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/dashboard")
    @Operation(summary = "Get current month summary", description = "Get dashboard summary for current month")
    public ResponseEntity<DashboardSummary> getCurrentMonthSummary() {
        try {
            DashboardSummary summary = usageService.getCurrentMonthSummary();
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/monthly/{year}/{month}")
    @Operation(summary = "Get monthly summary", description = "Get summary for specific year and month")
    public ResponseEntity<DashboardSummary> getMonthSummary(
            @PathVariable int year,
            @PathVariable int month) {
        try {
            DashboardSummary summary = usageService.getMonthSummary(year, month);
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/daily")
    @Operation(summary = "Get last 7 days usage", description = "Get daily usage for last 7 days")
    public ResponseEntity<List<UsageRecordResponse>> getLast7DaysUsage() {
        try {
            List<UsageRecordResponse> records = usageService.getLast7DaysUsage();
            return ResponseEntity.ok(records);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/daily/{days}")
    @Operation(summary = "Get daily usage for specific days", description = "Get daily usage for specified number of days")
    public ResponseEntity<List<UsageRecordResponse>> getDailyUsage(@PathVariable int days) {
        try {
            List<UsageRecordResponse> records = usageService.getDailyUsage(days);
            return ResponseEntity.ok(records);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/monthly/{year}/{month}/records")
    @Operation(summary = "Get monthly usage records", description = "Get all usage records for specific year and month")
    public ResponseEntity<List<UsageRecordResponse>> getMonthlyUsage(
            @PathVariable int year,
            @PathVariable int month) {
        try {
            List<UsageRecordResponse> records = usageService.getMonthlyUsage(year, month);
            return ResponseEntity.ok(records);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}