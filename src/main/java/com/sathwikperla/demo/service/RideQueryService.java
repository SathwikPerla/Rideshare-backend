package com.sathwikperla.demo.service;

import com.sathwikperla.demo.model.Ride;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class RideQueryService {

    private final MongoTemplate mongoTemplate;

    public RideQueryService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    // 1) Search pickup OR drop by keyword (case-insensitive)
    public List<Ride> searchByLocation(String text) {
        Query query = new Query();
        query.addCriteria(new Criteria().orOperator(
                Criteria.where("pickupLocation").regex(text, "i"),
                Criteria.where("dropLocation").regex(text, "i")
        ));
        return mongoTemplate.find(query, Ride.class);
    }

    // 2) Filter by distance range
    public List<Ride> filterByDistance(double min, double max) {
        Query query = new Query();
        query.addCriteria(Criteria.where("distanceKm").gte(min).lte(max));
        return mongoTemplate.find(query, Ride.class);
    }

    // 3) Filter by date range
    public List<Ride> filterByDateRange(LocalDate start, LocalDate end) {
        Query query = new Query();
        query.addCriteria(
                Criteria.where("createdDate").gte(start).lte(end)
        );
        return mongoTemplate.find(query, Ride.class);
    }

    // 4) Sort by fare
    public List<Ride> sortByFare(String order) {
        Sort.Direction direction = "asc".equalsIgnoreCase(order)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;
        Query query = new Query().with(Sort.by(direction, "fare"));
        return mongoTemplate.find(query, Ride.class);
    }

    // 5) All rides for a user
    public List<Ride> findByUser(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        return mongoTemplate.find(query, Ride.class);
    }

    // 6) Rides for user by status
    public List<Ride> findByUserAndStatus(String userId, String status) {
        Query query = new Query();
        query.addCriteria(
                Criteria.where("userId").is(userId)
                        .and("status").is(status)
        );
        return mongoTemplate.find(query, Ride.class);
    }

    // 7) Driver active rides (e.g., ACCEPTED)
    public List<Ride> findActiveRidesForDriver(String driverId) {
        Query query = new Query();
        query.addCriteria(
                Criteria.where("driverId").is(driverId)
                        .and("status").is("ACCEPTED")
        );
        return mongoTemplate.find(query, Ride.class);
    }

    // 8) Status + keyword filter
    public List<Ride> filterByStatusAndKeyword(String status, String search) {
        Query query = new Query();

        Criteria orKeyword = new Criteria().orOperator(
                Criteria.where("pickupLocation").regex(search, "i"),
                Criteria.where("dropLocation").regex(search, "i")
        );

        query.addCriteria(
                new Criteria().andOperator(
                        Criteria.where("status").is(status),
                        orKeyword
                )
        );
        return mongoTemplate.find(query, Ride.class);
    }

    // 9) Advanced search + sort + pagination
    public Page<Ride> advancedSearch(
            String search,
            String status,
            String sortField,
            String order,
            int page,
            int size
    ) {
        Query query = new Query();
        List<Criteria> andCriteria = new ArrayList<>();

        if (status != null && !status.isBlank()) {
            andCriteria.add(Criteria.where("status").is(status));
        }

        if (search != null && !search.isBlank()) {
            Criteria keyword = new Criteria().orOperator(
                    Criteria.where("pickupLocation").regex(search, "i"),
                    Criteria.where("dropLocation").regex(search, "i")
            );
            andCriteria.add(keyword);
        }

        if (!andCriteria.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(andCriteria.toArray(new Criteria[0])));
        }

        // Sorting
        if (sortField != null && !sortField.isBlank()) {
            Sort.Direction direction = "asc".equalsIgnoreCase(order)
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC;
            query.with(Sort.by(direction, sortField));
        }

        // Pagination
        Pageable pageable = PageRequest.of(page, size);
        long total = mongoTemplate.count(query, Ride.class);
        query.with(pageable);

        List<Ride> rides = mongoTemplate.find(query, Ride.class);
        return new PageImpl<>(rides, pageable, total);
    }

    // 14) Rides on a specific date
    public List<Ride> findByDate(LocalDate date) {
        Query query = new Query();
        query.addCriteria(Criteria.where("createdDate").is(date));
        return mongoTemplate.find(query, Ride.class);
    }
}
