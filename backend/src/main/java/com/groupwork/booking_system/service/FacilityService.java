package com.groupwork.booking_system.service;
import com.groupwork.booking_system.exception.ResourceNotFoundException;
import com.groupwork.booking_system.model.Facility;
import com.groupwork.booking_system.repository.FacilityRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FacilityService {
    private final FacilityRepository repo;
    public FacilityService(FacilityRepository repo) { this.repo = repo; }

    public List<Facility> getAllFacilities() { return repo.findAll(); }

    public Facility getFacilityById(Long id) {
        return repo.findById(id).orElseThrow(() ->
            new ResourceNotFoundException("Facility not found with id: " + id));
    }
    public Facility createFacility(Facility f) { return repo.save(f); }
    public Facility updateFacility(Long id, Facility updated) {
        Facility existing = getFacilityById(id);
        existing.setName(updated.getName());
        existing.setLocation(updated.getLocation());
        existing.setCapacity(updated.getCapacity());
        return repo.save(existing);
    }
    public void deleteFacility(Long id) { repo.delete(getFacilityById(id)); }
}