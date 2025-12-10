package com.sathwikperla.demo.dto.analytics;

public class StatusSummaryDto {
    private String status;
    private long count;

    public StatusSummaryDto(String status, long count) {
        this.status = status;
        this.count = count;
    }

    // getters
}
