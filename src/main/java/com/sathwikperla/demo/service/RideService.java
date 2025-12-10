package com.sathwikperla.demo.service;

import com.sathwikperla.demo.dto.CreateRideRequest;
import com.sathwikperla.demo.exception.BadRequestException;
import com.sathwikperla.demo.exception.NotFoundException;
import com.sathwikperla.demo.model.Ride;
import com.sathwikperla.demo.model.User;
import com.sathwikperla.demo.repository.RideRepository;
import com.sathwikperla.demo.repository.UserRepository;
import com.sathwikperla.demo.util.SecurityUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class RideService {

    private final RideRepository rideRepository;
    private final UserRepository userRepository;

    public RideService(RideRepository rideRepository,
                       UserRepository userRepository) {
        this.rideRepository = rideRepository;
        this.userRepository = userRepository;
    }

    private User getCurrentUser() {
        String username = SecurityUtil.getCurrentUsername();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Current user not found"));
    }

    public Ride createRide(CreateRideRequest request) {
        User user = getCurrentUser();

        if (!"ROLE_USER".equals(user.getRole())) {
            throw new BadRequestException("Only users can request rides");
        }

        Ride ride = new Ride();
        ride.setUserId(user.getId());
        ride.setPickupLocation(request.getPickupLocation());
        ride.setDropLocation(request.getDropLocation());

        // ✅ REQUIRED FIELDS (Step 2)
        ride.setCreatedDate(LocalDate.now());
        ride.setCreatedAt(new Date());
        ride.setStatus("REQUESTED");

        // ✅ Temporary fallback so queries don’t break
        if (ride.getDistanceKm() == null) {
            ride.setDistanceKm(5.0);
        }
        if (ride.getFare() == null) {
            ride.setFare(100.0);
        }
        return rideRepository.save(ride);
    }

    public List<Ride> getUserRides() {
        User user = getCurrentUser();
        return rideRepository.findByUserId(user.getId());
    }

    public List<Ride> getPendingRidesForDriver() {
        User driver = getCurrentUser();

        if (!"ROLE_DRIVER".equals(driver.getRole())) {
            throw new BadRequestException("Only drivers can view pending rides");
        }

        return rideRepository.findByStatus("REQUESTED");
    }

    public Ride acceptRide(String rideId) {
        User driver = getCurrentUser();

        if (!"ROLE_DRIVER".equals(driver.getRole())) {
            throw new BadRequestException("Only drivers can accept rides");
        }

        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new NotFoundException("Ride not found"));

        if (!"REQUESTED".equals(ride.getStatus())) {
            throw new BadRequestException("Ride not in REQUESTED state");
        }

        ride.setDriverId(driver.getId());
        ride.setStatus("ACCEPTED");
        return rideRepository.save(ride);
    }

    public Ride completeRide(String rideId) {
        User current = getCurrentUser(); // optional ownership checks later

        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new NotFoundException("Ride not found"));

        if (!"ACCEPTED".equals(ride.getStatus())) {
            throw new BadRequestException("Ride not in ACCEPTED state");
        }

        ride.setStatus("COMPLETED");
        return rideRepository.save(ride);
    }
}
