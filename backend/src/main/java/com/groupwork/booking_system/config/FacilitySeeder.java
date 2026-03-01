package com.groupwork.booking_system.config;

import com.groupwork.booking_system.model.Facility;
import com.groupwork.booking_system.repository.FacilityRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FacilitySeeder {

    @Bean
    public CommandLineRunner seedFacilities(FacilityRepository facilityRepository) {
        return args -> {
            if (facilityRepository.count() == 0) {
                Facility f1 = new Facility();
                f1.setName("Conference Room A");
                f1.setLocation("Building 1, Floor 2");
                f1.setCapacity(20);
                facilityRepository.save(f1);

                Facility f2 = new Facility();
                f2.setName("Meeting Room B");
                f2.setLocation("Building 2, Floor 1");
                f2.setCapacity(10);
                facilityRepository.save(f2);

                Facility f3 = new Facility();
                f3.setName("Auditorium");
                f3.setLocation("Building 3, Ground Floor");
                f3.setCapacity(100);
                facilityRepository.save(f3);

                Facility f4 = new Facility();
                f4.setName("Training Lab");
                f4.setLocation("Building 1, Floor 3");
                f4.setCapacity(30);
                facilityRepository.save(f4);

                Facility f5 = new Facility();
                f5.setName("Study Room 1");
                f5.setLocation("Library, Floor 1");
                f5.setCapacity(8);
                facilityRepository.save(f5);

                Facility f6 = new Facility();
                f6.setName("Computer Lab");
                f6.setLocation("Building 4, Floor 2");
                f6.setCapacity(50);
                facilityRepository.save(f6);

                System.out.println("=== Default facilities seeded ===");
            }
        };
    }
}
