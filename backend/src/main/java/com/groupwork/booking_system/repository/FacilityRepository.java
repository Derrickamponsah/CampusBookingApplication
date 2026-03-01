package com.groupwork.booking_system.repository;
import com.groupwork.booking_system.model.Facility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long> {}