package com.sathwikperla.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Date;

@Document(collection = "rides")
public class Ride {

    @Id
    private String id;

    // Passenger (USER)
    private String userId;

    // Driver (ROLE_DRIVER) – null until accepted
    private String driverId;

    // Ride details
    private String pickupLocation;
    private String dropLocation;

    // REQUESTED / ACCEPTED / COMPLETED
    private String status;

    // Timing
    private Date createdAt;        // full timestamp
    private LocalDate createdDate; // date-only (for queries + analytics)

    // Ride metrics
    private Double distanceKm;     // for distance filters & avgDistance
    private Double fare;           // for sorting & earnings analytics

    public Ride() {
    }

    public Ride(String id,
                String userId,
                String driverId,
                String pickupLocation,
                String dropLocation,
                String status,
                Date createdAt,
                LocalDate createdDate,
                Double distanceKm,
                Double fare) {
        this.id = id;
        this.userId = userId;
        this.driverId = driverId;
        this.pickupLocation = pickupLocation;
        this.dropLocation = dropLocation;
        this.status = status;
        this.createdAt = createdAt;
        this.createdDate = createdDate;
        this.distanceKm = distanceKm;
        this.fare = fare;
    }

    // ---------- Getters & Setters ----------

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getDropLocation() {
        return dropLocation;
    }

    public void setDropLocation(String dropLocation) {
        this.dropLocation = dropLocation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public Double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(Double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public Double getFare() {
        return fare;
    }

    public void setFare(Double fare) {
        this.fare = fare;
    }
}
