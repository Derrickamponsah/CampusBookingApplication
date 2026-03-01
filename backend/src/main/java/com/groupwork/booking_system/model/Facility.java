package com.groupwork.booking_system.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "facilities")
public class Facility {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String name;
    @NotBlank(message = "Location is required")
    @Column(nullable = false)
    private String location;
    @Min(1) @Column(nullable = false)
    private Integer capacity;

    public Facility() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }
}