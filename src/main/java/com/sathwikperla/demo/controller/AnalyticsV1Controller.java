package com.sathwikperla.demo.controller;

import com.sathwikperla.demo.dto.analytics.*;
import com.sathwikperla.demo.service.AnalyticsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/analytics")
public class AnalyticsV1Controller {

    private final AnalyticsService analyticsService;

    public AnalyticsV1Controller(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    // 10) Rides per day
    @GetMapping("/rides-per-day")
    public List<RidesPerDayDto> ridesPerDay() {
        return analyticsService.ridesPerDay();
    }

    // 11) Driver summary
    @GetMapping("/driver/{driverId}/summary")
    public DriverSummaryDto driverSummary(@PathVariable String driverId) {
        return analyticsService.driverSummary(driverId);
    }

    // 12) User spending
    @GetMapping("/user/{userId}/spending")
    public UserSpendingDto userSpending(@PathVariable String userId) {
        return analyticsService.userSpending(userId);
    }

    // 13) Status summary
    @GetMapping("/status-summary")
    public List<StatusSummaryDto> statusSummary() {
        return analyticsService.statusSummary();
    }
}
