package com.groupwork.booking_system.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "bookings")
public class Booking {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "facility_id", nullable = false)
    private Facility facility;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @NotNull @Column(nullable = false)
    private LocalDate date;
    @NotNull @Column(name = "start_time", nullable = false)
    private LocalTime startTime;
    @NotNull @Column(name = "end_time", nullable = false)
    private LocalTime endTime;
    @Column(nullable = false)
    private String status = "CONFIRMED";

    public Booking() {}
    public Booking(Facility facility, User user, LocalDate date, LocalTime startTime, LocalTime endTime, String status) {
        this.facility = facility; this.user = user; this.date = date;
        this.startTime = startTime; this.endTime = endTime; this.status = status;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Facility getFacility() { return facility; }
    public void setFacility(Facility facility) { this.facility = facility; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}