package com.sathwikperla.demo.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateRideRequest {

    @NotBlank(message = "Pickup is required")
    private String pickupLocation;

    @NotBlank(message = "Drop is required")
    private String dropLocation;

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
}
