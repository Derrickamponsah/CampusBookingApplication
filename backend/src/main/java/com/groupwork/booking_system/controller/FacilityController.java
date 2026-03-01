package com.groupwork.booking_system.controller;

import com.groupwork.booking_system.model.Facility;
import com.groupwork.booking_system.service.FacilityService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/facilities")
@CrossOrigin(origins = "*")
public class FacilityController {
    private final FacilityService service;

    public FacilityController(FacilityService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Facility>> getAll() {
        return ResponseEntity.ok(service.getAllFacilities());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Facility> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getFacilityById(id));
    }

    @PostMapping
    public ResponseEntity<Facility> create(@Valid @RequestBody Facility f) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createFacility(f));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Facility> update(@PathVariable Long id, @Valid @RequestBody Facility f) {
        return ResponseEntity.ok(service.updateFacility(id, f));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteFacility(id);
        return ResponseEntity.noContent().build();
    }
}