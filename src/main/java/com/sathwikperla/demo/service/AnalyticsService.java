package com.sathwikperla.demo.service;

import com.sathwikperla.demo.dto.analytics.*;
import org.bson.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
public class AnalyticsService {

    private final MongoTemplate mongoTemplate;

    public AnalyticsService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    // 10) Rides per day
    public List<RidesPerDayDto> ridesPerDay() {
        var agg = newAggregation(
                group("createdDate").count().as("count"),
                sort(Sort.Direction.ASC, "_id")
        );

        AggregationResults<Document> results =
                mongoTemplate.aggregate(agg, "rides", Document.class);

        return results.getMappedResults().stream()
                .map(doc -> {
                    Object id = doc.get("_id");
                    LocalDate date = convertIdToLocalDate(id);
                    long count = getLongSafely(doc, "count");
                    return new RidesPerDayDto(date, count);
                })
                .collect(Collectors.toList());
    }

    // 11) Driver summary
    public DriverSummaryDto driverSummary(String driverId) {
        var agg = newAggregation(
                match(org.springframework.data.mongodb.core.query.Criteria.where("driverId").is(driverId)),
                group("driverId")
                        .count().as("totalRides")
                        .sum(org.springframework.data.mongodb.core.aggregation.ConditionalOperators
                                .when(org.springframework.data.mongodb.core.query.Criteria.where("status").is("COMPLETED"))
                                .then(1).otherwise(0)).as("completedRides")
                        .sum(org.springframework.data.mongodb.core.aggregation.ConditionalOperators
                                .when(org.springframework.data.mongodb.core.query.Criteria.where("status").is("CANCELLED"))
                                .then(1).otherwise(0)).as("cancelledRides")
                        .avg("distanceKm").as("avgDistance")
                        .sum("fare").as("totalFare")
        );

        Document result = mongoTemplate.aggregate(agg, "rides", Document.class)
                .getUniqueMappedResult();

        DriverSummaryDto dto = new DriverSummaryDto();
        dto.setDriverId(driverId);

        if (result == null) {
            dto.setTotalRides(0L);
            dto.setCompletedRides(0L);
            dto.setCancelledRides(0L);
            dto.setAvgDistance(0.0);
            dto.setTotalFare(0.0);
            return dto;
        }

        dto.setTotalRides(getLongSafely(result, "totalRides"));
        dto.setCompletedRides(getLongSafely(result, "completedRides"));
        dto.setCancelledRides(getLongSafely(result, "cancelledRides"));
        dto.setAvgDistance(getDoubleSafely(result, "avgDistance"));
        dto.setTotalFare(getDoubleSafely(result, "totalFare"));
        return dto;
    }

    // 12) User spending (COMPLETED rides only)
    public UserSpendingDto userSpending(String userId) {
        var agg = newAggregation(
                match(org.springframework.data.mongodb.core.query.Criteria
                        .where("userId").is(userId)
                        .and("status").is("COMPLETED")),
                group("userId")
                        .count().as("completedRides")
                        .sum("fare").as("totalSpent")
        );

        Document result = mongoTemplate.aggregate(agg, "rides", Document.class)
                .getUniqueMappedResult();

        UserSpendingDto dto = new UserSpendingDto();
        dto.setUserId(userId);

        if (result == null) {
            dto.setCompletedRides(0L);
            dto.setTotalSpent(0.0);
            return dto;
        }

        dto.setCompletedRides(getLongSafely(result, "completedRides"));
        dto.setTotalSpent(getDoubleSafely(result, "totalSpent"));
        return dto;
    }

    // 13) Status summary
    public List<StatusSummaryDto> statusSummary() {
        var agg = newAggregation(
                group("status").count().as("count")
        );

        AggregationResults<Document> results =
                mongoTemplate.aggregate(agg, "rides", Document.class);

        return results.getMappedResults().stream()
                .map(doc -> new StatusSummaryDto(
                        doc.getString("_id"),
                        getLongSafely(doc, "count")
                ))
                .collect(Collectors.toList());
    }

    // -------- helpers --------
    private static LocalDate convertIdToLocalDate(Object id) {
        if (id == null) return null;
        if (id instanceof LocalDate) return (LocalDate) id;
        if (id instanceof String) {
            return LocalDate.parse((String) id);
        }
        if (id instanceof java.util.Date) {
            Instant ins = ((java.util.Date) id).toInstant();
            return ins.atZone(ZoneId.systemDefault()).toLocalDate();
        }
        // fallback: try toString -> parse
        return LocalDate.parse(id.toString());
    }

    private static long getLongSafely(Document doc, String key) {
        Object o = doc.get(key);
        if (o == null) return 0L;
        if (o instanceof Number) return ((Number) o).longValue();
        try { return Long.parseLong(o.toString()); } catch (Exception e) { return 0L; }
    }

    private static double getDoubleSafely(Document doc, String key) {
        Object o = doc.get(key);
        if (o == null) return 0.0;
        if (o instanceof Number) return ((Number) o).doubleValue();
        try { return Double.parseDouble(o.toString()); } catch (Exception e) { return 0.0; }
    }
}
