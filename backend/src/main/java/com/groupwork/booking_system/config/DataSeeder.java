package com.groupwork.booking_system.config;

import com.groupwork.booking_system.model.User;
import com.groupwork.booking_system.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {

    @Bean
    public CommandLineRunner seedUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.count() == 0) {
                User john = new User();
                john.setName("John Doe");
                john.setEmail("john@gmail.com");
                john.setPassword(passwordEncoder.encode("password123"));
                john.setRole("USER");
                userRepository.save(john);

                User nkansah = new User();
                nkansah.setName("Nana Kansah");
                nkansah.setEmail("nkansah@gmail.com");
                nkansah.setPassword(passwordEncoder.encode("admin123"));
                nkansah.setRole("ADMIN");
                userRepository.save(nkansah);

                User bob = new User();
                bob.setName("Bob Johnson");
                bob.setEmail("bob@gmail.com");
                bob.setPassword(passwordEncoder.encode("password123"));
                bob.setRole("USER");
                userRepository.save(bob);

                User alice = new User();
                alice.setName("Alice Williams");
                alice.setEmail("alice@gmail.com");
                alice.setPassword(passwordEncoder.encode("password123"));
                alice.setRole("USER");
                userRepository.save(alice);

                System.out.println("=== Default users seeded ===");
                System.out.println("USER:  john@gmail.com / password123");
                System.out.println("ADMIN: nkansah@gmail.com / admin123");
                System.out.println("USER:  bob@gmail.com / password123");
                System.out.println("USER:  alice@gmail.com / password123");
            }
        };
    }
}
