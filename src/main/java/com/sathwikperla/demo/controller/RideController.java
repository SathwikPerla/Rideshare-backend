package com.sathwikperla.demo.controller;

import com.sathwikperla.demo.dto.CreateRideRequest;
import com.sathwikperla.demo.model.Ride;
import com.sathwikperla.demo.service.RideService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class RideController {

    private final RideService rideService;

    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    // USER: Create Ride
    @PostMapping("/rides")
    public ResponseEntity<Ride> createRide(@Valid @RequestBody CreateRideRequest request) {
        return ResponseEntity.ok(rideService.createRide(request));
    }

    // USER: Get their own rides
    @GetMapping("/user/rides")
    public ResponseEntity<List<Ride>> getUserRides() {
        return ResponseEntity.ok(rideService.getUserRides());
    }

    // DRIVER: View pending ride requests
    @GetMapping("/driver/rides/requests")
    public ResponseEntity<List<Ride>> getPendingRides() {
        return ResponseEntity.ok(rideService.getPendingRidesForDriver());
    }

    // DRIVER: Accept a ride
    @PostMapping("/driver/rides/{id}/accept")
    public ResponseEntity<Ride> acceptRide(@PathVariable String id) {
        return ResponseEntity.ok(rideService.acceptRide(id));
    }

    // USER / DRIVER: Complete ride
    @PostMapping("/rides/{id}/complete")
    public ResponseEntity<Ride> completeRide(@PathVariable String id) {
        return ResponseEntity.ok(rideService.completeRide(id));
    }
}
