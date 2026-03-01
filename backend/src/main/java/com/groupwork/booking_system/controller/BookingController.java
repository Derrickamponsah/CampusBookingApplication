package com.groupwork.booking_system.controller;

import com.groupwork.booking_system.dto.BookingRequest;
import com.groupwork.booking_system.model.Booking;
import com.groupwork.booking_system.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class BookingController {
    private final BookingService service;

    public BookingController(BookingService service) {
        this.service = service;
    }

    @GetMapping("/bookings")
    public ResponseEntity<List<Booking>> getAll() {
        return ResponseEntity.ok(service.getAllBookings());
    }

    @GetMapping("/bookings/{id}")
    public ResponseEntity<Booking> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getBookingById(id));
    }

    @PostMapping("/bookings")
    public ResponseEntity<Booking> create(@Valid @RequestBody BookingRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createBooking(req));
    }

    @PutMapping("/bookings/{id}")
    public ResponseEntity<Booking> update(@PathVariable Long id, @Valid @RequestBody BookingRequest req) {
        return ResponseEntity.ok(service.updateBooking(id, req));
    }

    @DeleteMapping("/bookings/{id}")
    public ResponseEntity<Map<String, String>> cancel(@PathVariable Long id) {
        service.cancelBooking(id);
        return ResponseEntity.ok(Map.of("message", "Booking " + id + " cancelled.", "status", "CANCELLED"));
    }

    @GetMapping("/availability")
    public ResponseEntity<Map<String, Object>> availability(
            @RequestParam Long facilityId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime) {
        return ResponseEntity.ok(service.checkAvailability(facilityId, date, startTime, endTime));
    }
}