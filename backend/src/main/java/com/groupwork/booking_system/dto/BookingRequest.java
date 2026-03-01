package com.groupwork.booking_system.dto;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class BookingRequest {
    @NotNull private Long facilityId;
    @NotNull private Long userId;
    @NotNull @FutureOrPresent private LocalDate date;
    @NotNull private LocalTime startTime;
    @NotNull private LocalTime endTime;
    private String status = "CONFIRMED";

    public Long getFacilityId() { return facilityId; }
    public void setFacilityId(Long facilityId) { this.facilityId = facilityId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}