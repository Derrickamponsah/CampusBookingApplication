package com.groupwork.booking_system.repository;

import com.groupwork.booking_system.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b WHERE b.facility.id = :facilityId " +
            "AND b.date = :date AND b.status != 'CANCELLED' " +
            "AND b.startTime < :endTime AND b.endTime > :startTime")
    List<Booking> findConflictingBookings(@Param("facilityId") Long facilityId,
            @Param("date") LocalDate date, @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime);

    @Query("SELECT b FROM Booking b WHERE b.facility.id = :facilityId " +
            "AND b.date = :date AND b.status != 'CANCELLED' AND b.id != :excludeId " +
            "AND b.startTime < :endTime AND b.endTime > :startTime")
    List<Booking> findConflictingBookingsExcluding(@Param("facilityId") Long facilityId,
            @Param("date") LocalDate date, @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime, @Param("excludeId") Long excludeId);

    @Query("SELECT b FROM Booking b WHERE b.facility.id = :facilityId " +
            "AND b.date = :date AND b.status != 'CANCELLED' ORDER BY b.startTime")
    List<Booking> findActiveBookingsByFacilityAndDate(
            @Param("facilityId") Long facilityId, @Param("date") LocalDate date);

    @Query("SELECT b FROM Booking b WHERE b.user.id = :userId ORDER BY b.date DESC, b.startTime DESC")
    List<Booking> findByUserId(@Param("userId") Long userId);

    @Query("SELECT b FROM Booking b WHERE b.user.id = :userId AND b.status = :status ORDER BY b.date DESC")
    List<Booking> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") String status);
}