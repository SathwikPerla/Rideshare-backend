package com.sathwikperla.demo.controller;

import com.sathwikperla.demo.model.Ride;
import com.sathwikperla.demo.service.RideQueryService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/rides")
public class RideQueryController {

    private final RideQueryService rideQueryService;

    public RideQueryController(RideQueryService rideQueryService) {
        this.rideQueryService = rideQueryService;
    }

    // 1) Search rides by keyword
    @GetMapping("/search")
    public List<Ride> search(@RequestParam String text) {
        return rideQueryService.searchByLocation(text);
    }

    // 2) Filter by distance
    @GetMapping("/filter-distance")
    public List<Ride> filterDistance(
            @RequestParam double min,
            @RequestParam double max
    ) {
        return rideQueryService.filterByDistance(min, max);
    }

    // 3) Date range
    @GetMapping("/filter-date-range")
    public List<Ride> filterDateRange(
            @RequestParam String start,
            @RequestParam String end
    ) {
        return rideQueryService.filterByDateRange(
                LocalDate.parse(start),
                LocalDate.parse(end)
        );
    }

    // 4) Sort by fare
    @GetMapping("/sort")
    public List<Ride> sort(@RequestParam(defaultValue = "asc") String order) {
        return rideQueryService.sortByFare(order);
    }

    // 5) User rides
    @GetMapping("/user/{userId}")
    public List<Ride> userRides(@PathVariable String userId) {
        return rideQueryService.findByUser(userId);
    }

    // 6) User rides by status
    @GetMapping("/user/{userId}/status/{status}")
    public List<Ride> userRidesByStatus(
            @PathVariable String userId,
            @PathVariable String status
    ) {
        return rideQueryService.findByUserAndStatus(userId, status);
    }

    // 7) Driver active rides
    @GetMapping("/driver/{driverId}/active-rides")
    public List<Ride> driverActiveRides(@PathVariable String driverId) {
        return rideQueryService.findActiveRidesForDriver(driverId);
    }

    // 8) Filter by status + keyword
    @GetMapping("/filter-status")
    public List<Ride> filterStatus(
            @RequestParam String status,
            @RequestParam String search
    ) {
        return rideQueryService.filterByStatusAndKeyword(status, search);
    }

    // 9) Advanced search
    @GetMapping("/advanced-search")
    public Page<Ride> advancedSearch(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "fare") String sort,
            @RequestParam(defaultValue = "asc") String order,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return rideQueryService.advancedSearch(search, status, sort, order, page, size);
    }

    // 14) Rides on specific date
    @GetMapping("/date/{date}")
    public List<Ride> ridesByDate(@PathVariable String date) {
        return rideQueryService.findByDate(LocalDate.parse(date));
    }
}
