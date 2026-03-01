package com.groupwork.booking_system.service;

import com.groupwork.booking_system.dto.BookingRequest;
import com.groupwork.booking_system.exception.*;
import com.groupwork.booking_system.model.*;
import com.groupwork.booking_system.repository.BookingRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BookingService {
    private final BookingRepository bookingRepo;
    private final FacilityService facilityService;
    private final UserService userService;

    public BookingService(BookingRepository bookingRepo, FacilityService facilityService, UserService userService) {
        this.bookingRepo = bookingRepo;
        this.facilityService = facilityService;
        this.userService = userService;
    }

    public List<Booking> getAllBookings() {
        return bookingRepo.findAll();
    }

    public Booking getBookingById(Long id) {
        return bookingRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));
    }

    public List<Booking> getBookingsByUserId(Long userId) {
        return bookingRepo.findByUserId(userId);
    }

    public Booking createBooking(BookingRequest req) {
        validateTime(req.getStartTime(), req.getEndTime());
        Facility facility = facilityService.getFacilityById(req.getFacilityId());
        User user = userService.getUserById(req.getUserId());
        List<Booking> conflicts = bookingRepo.findConflictingBookings(
                req.getFacilityId(), req.getDate(), req.getStartTime(), req.getEndTime());
        if (!conflicts.isEmpty())
            throw new BookingConflictException("'" + facility.getName() + "' is already booked for that time slot.");
        return bookingRepo.save(new Booking(facility, user, req.getDate(),
                req.getStartTime(), req.getEndTime(),
                req.getStatus() != null ? req.getStatus() : "CONFIRMED"));
    }

    public Booking updateBooking(Long id, BookingRequest req) {
        Booking existing = getBookingById(id);
        validateTime(req.getStartTime(), req.getEndTime());
        Facility facility = facilityService.getFacilityById(req.getFacilityId());
        User user = userService.getUserById(req.getUserId());
        List<Booking> conflicts = bookingRepo.findConflictingBookingsExcluding(
                req.getFacilityId(), req.getDate(), req.getStartTime(), req.getEndTime(), id);
        if (!conflicts.isEmpty())
            throw new BookingConflictException("'" + facility.getName() + "' is already booked for that time slot.");
        existing.setFacility(facility);
        existing.setUser(user);
        existing.setDate(req.getDate());
        existing.setStartTime(req.getStartTime());
        existing.setEndTime(req.getEndTime());
        if (req.getStatus() != null)
            existing.setStatus(req.getStatus());
        return bookingRepo.save(existing);
    }

    public void cancelBooking(Long id) {
        Booking b = getBookingById(id);
        b.setStatus("CANCELLED");
        bookingRepo.save(b);
    }

    public Booking saveBooking(Booking booking) {
        return bookingRepo.save(booking);
    }

    public Map<String, Object> checkAvailability(Long facilityId, LocalDate date,
            LocalTime startTime, LocalTime endTime) {
        Facility facility = facilityService.getFacilityById(facilityId);
        Map<String, Object> result = new HashMap<>();
        result.put("facilityId", facilityId);
        result.put("facilityName", facility.getName());
        result.put("date", date.toString());

        if (startTime != null && endTime != null) {
            validateTime(startTime, endTime);
            boolean available = bookingRepo.findConflictingBookings(
                    facilityId, date, startTime, endTime).isEmpty();
            result.put("startTime", startTime.toString());
            result.put("endTime", endTime.toString());
            result.put("available", available);
            result.put("message", available ? "Time slot is available." : "Time slot is not available.");
        } else {
            List<Booking> bookings = bookingRepo.findActiveBookingsByFacilityAndDate(facilityId, date);
            result.put("bookingCount", bookings.size());
            result.put("existingBookings", bookings.stream().map(b -> {
                Map<String, String> slot = new HashMap<>();
                slot.put("startTime", b.getStartTime().toString());
                slot.put("endTime", b.getEndTime().toString());
                slot.put("status", b.getStatus());
                return slot;
            }).toList());
        }
        return result;
    }

    private void validateTime(LocalTime start, LocalTime end) {
        if (!end.isAfter(start))
            throw new IllegalArgumentException("End time must be after start time.");
        
        // Validate 30-minute slots - times must be in 30-minute intervals (00, 30)
        if (start.getMinute() % 30 != 0 || start.getSecond() != 0)
            throw new IllegalArgumentException("Start time must be on a 30-minute interval (00 or 30 minutes).");
        
        if (end.getMinute() % 30 != 0 || end.getSecond() != 0)
            throw new IllegalArgumentException("End time must be on a 30-minute interval (00 or 30 minutes).");
        
        // Validate 30-minute duration - bookings must be exactly 30 minutes
        long durationMinutes = java.time.temporal.ChronoUnit.MINUTES.between(start, end);
        if (durationMinutes != 30L)
            throw new IllegalArgumentException("Booking duration must be exactly 30 minutes. Multiple 30-minute slots can be booked separately.");
    }
}