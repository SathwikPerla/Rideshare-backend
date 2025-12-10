package com.sathwikperla.demo.controller;

import com.sathwikperla.demo.dto.CreateRideRequest;
import com.sathwikperla.demo.model.Ride;
import com.sathwikperla.demo.service.RideService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rides")
public class RideController {

    private final RideService rideService;

    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    // create ride (ROLE_USER)
    @PostMapping
    public Ride createRide(@Valid @RequestBody CreateRideRequest request) {
        return rideService.createRide(request);
    }

    // get current user's rides
    @GetMapping
    public List<Ride> getUserRides() {
        return rideService.getUserRides();
    }

    // driver accepts ride
    @PostMapping("/{id}/accept")
    public Ride acceptRide(@PathVariable String id) {
        return rideService.acceptRide(id);
    }

    // complete ride
    @PostMapping("/{id}/complete")
    public Ride completeRide(@PathVariable String id) {
        return rideService.completeRide(id);
    }
}
