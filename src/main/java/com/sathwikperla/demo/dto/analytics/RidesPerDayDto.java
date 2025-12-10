package com.sathwikperla.demo.dto.analytics;

import java.time.LocalDate;

public class RidesPerDayDto {
    private LocalDate date;
    private long count;

    public RidesPerDayDto(LocalDate date, long count) {
        this.date = date;
        this.count = count;
    }

    public LocalDate getDate() { return date; }
    public long getCount() { return count; }
}
