package com.groupwork.booking_system.controller;

import com.groupwork.booking_system.dto.BookingRequest;
import com.groupwork.booking_system.model.Booking;
import com.groupwork.booking_system.model.Facility;
import com.groupwork.booking_system.model.User;
import com.groupwork.booking_system.service.BookingService;
import com.groupwork.booking_system.service.FacilityService;
import com.groupwork.booking_system.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Controller
@RequestMapping("/dashboard")
public class PageController {

    private final FacilityService facilityService;
    private final BookingService bookingService;
    private final UserService userService;

    public PageController(FacilityService facilityService, BookingService bookingService, UserService userService) {
        this.facilityService = facilityService;
        this.bookingService = bookingService;
        this.userService = userService;
    }

    @GetMapping
    public String dashboard(Authentication auth, Model model) {
        User user = getCurrentUser(auth);
        model.addAttribute("user", user);
        model.addAttribute("facilities", facilityService.getAllFacilities());

        List<Booking> userBookings = bookingService.getBookingsByUserId(user.getId());
        long confirmedCount = userBookings.stream().filter(b -> "CONFIRMED".equals(b.getStatus())).count();
        long cancelledCount = userBookings.stream().filter(b -> "CANCELLED".equals(b.getStatus())).count();

        model.addAttribute("totalBookings", userBookings.size());
        model.addAttribute("confirmedCount", confirmedCount);
        model.addAttribute("cancelledCount", cancelledCount);
        model.addAttribute("recentBookings", userBookings.stream().limit(5).toList());

        return "dashboard";
    }

    @GetMapping("/facilities")
    public String facilities(Model model, Authentication auth) {
        model.addAttribute("user", getCurrentUser(auth));
        model.addAttribute("facilities", facilityService.getAllFacilities());
        return "facilities";
    }

    @GetMapping("/facilities/{id}/availability")
    public String facilityAvailability(@PathVariable Long id,
            @RequestParam(required = false) String date,
            Model model, Authentication auth) {
        User user = getCurrentUser(auth);
        model.addAttribute("user", user);
        Facility facility = facilityService.getFacilityById(id);
        model.addAttribute("facility", facility);

        LocalDate selectedDate = (date != null && !date.isEmpty()) ? LocalDate.parse(date) : LocalDate.now();
        model.addAttribute("selectedDate", selectedDate);

        Map<String, Object> availability = bookingService.checkAvailability(id, selectedDate, null, null);
        model.addAttribute("availability", availability);

        List<Map<String, Object>> slots = generateTimeSlots(id, selectedDate, availability);
        model.addAttribute("slots", slots);

        return "availability";
    }

    @GetMapping("/book")
    public String bookingForm(@RequestParam Long facilityId,
            @RequestParam String date,
            @RequestParam String startTime,
            @RequestParam String endTime,
            Model model, Authentication auth) {
        User user = getCurrentUser(auth);
        model.addAttribute("user", user);
        model.addAttribute("facility", facilityService.getFacilityById(facilityId));
        model.addAttribute("date", date);
        model.addAttribute("startTime", startTime);
        model.addAttribute("endTime", endTime);
        return "book";
    }

    @PostMapping("/book")
    public String createBooking(@RequestParam Long facilityId,
            @RequestParam String date,
            @RequestParam String startTime,
            @RequestParam String endTime,
            Authentication auth,
            RedirectAttributes redirectAttributes) {
        try {
            User user = getCurrentUser(auth);
            BookingRequest req = new BookingRequest();
            req.setFacilityId(facilityId);
            req.setUserId(user.getId());
            req.setDate(LocalDate.parse(date));
            req.setStartTime(LocalTime.parse(startTime));
            req.setEndTime(LocalTime.parse(endTime));
            req.setStatus("CONFIRMED");

            bookingService.createBooking(req);
            redirectAttributes.addFlashAttribute("success", "Booking created successfully!");
            return "redirect:/dashboard/bookings";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/dashboard/facilities/" + facilityId + "/availability?date=" + date;
        }
    }

    @GetMapping("/bookings")
    public String bookings(Authentication auth, Model model,
            @RequestParam(required = false) String status) {
        User user = getCurrentUser(auth);
        model.addAttribute("user", user);

        List<Booking> userBookings = bookingService.getBookingsByUserId(user.getId());
        if (status != null && !status.isEmpty() && !"ALL".equals(status)) {
            userBookings = userBookings.stream()
                    .filter(b -> status.equals(b.getStatus()))
                    .toList();
        }
        model.addAttribute("bookings", userBookings);
        model.addAttribute("selectedStatus", status != null ? status : "ALL");
        return "bookings";
    }

    @PostMapping("/bookings/{id}/cancel")
    public String cancelBooking(@PathVariable Long id, Authentication auth,
            RedirectAttributes redirectAttributes) {
        try {
            bookingService.cancelBooking(id);
            redirectAttributes.addFlashAttribute("success", "Booking cancelled successfully.");
            redirectAttributes.addAttribute("cancelled", "true");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/dashboard/bookings";
    }

    @GetMapping("/profile")
    public String profile(Authentication auth, Model model) {
        User user = getCurrentUser(auth);
        model.addAttribute("user", user);
        List<Booking> userBookings = bookingService.getBookingsByUserId(user.getId());
        model.addAttribute("totalBookings", userBookings.size());
        long activeBookings = userBookings.stream()
                .filter(b -> "CONFIRMED".equals(b.getStatus()))
                .count();
        model.addAttribute("activeBookings", activeBookings);
        return "profile";
    }

    @GetMapping("/admin/bookings")
    public String adminBookings(Authentication auth, Model model) {
        User user = getCurrentUser(auth);
        if (!"ADMIN".equals(user.getRole())) {
            return "redirect:/dashboard";
        }
        model.addAttribute("user", user);
        model.addAttribute("bookings", bookingService.getAllBookings());
        model.addAttribute("facilities", facilityService.getAllFacilities());
        return "admin-bookings";
    }

    @PostMapping("/admin/bookings/{id}/status")
    public String updateBookingStatus(@PathVariable Long id,
            @RequestParam String status,
            Authentication auth,
            RedirectAttributes redirectAttributes) {
        User user = getCurrentUser(auth);
        if (!"ADMIN".equals(user.getRole())) {
            return "redirect:/dashboard";
        }
        try {
            Booking booking = bookingService.getBookingById(id);
            booking.setStatus(status);
            bookingService.saveBooking(booking);
            redirectAttributes.addFlashAttribute("success", "Booking #" + id + " status updated to " + status);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/dashboard/admin/bookings";
    }

    private User getCurrentUser(Authentication auth) {
        if (auth == null) {
            throw new RuntimeException("Not authenticated");
        }
        String email = auth.getName();
        return userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> generateTimeSlots(Long facilityId, LocalDate date,
            Map<String, Object> availability) {
        List<Map<String, Object>> slots = new ArrayList<>();
        List<Map<String, String>> existingBookings = (List<Map<String, String>>) availability
                .getOrDefault("existingBookings", Collections.emptyList());

        LocalTime slotStart = LocalTime.of(7, 0);
        LocalTime dayEnd = LocalTime.of(21, 0);

        while (slotStart.isBefore(dayEnd)) {
            LocalTime slotEnd = slotStart.plusMinutes(30);
            Map<String, Object> slot = new HashMap<>();
            slot.put("startTime", slotStart.toString());
            slot.put("endTime", slotEnd.toString());

            boolean isBooked = false;
            for (Map<String, String> booking : existingBookings) {
                LocalTime bookStart = LocalTime.parse(booking.get("startTime"));
                LocalTime bookEnd = LocalTime.parse(booking.get("endTime"));
                if (slotStart.isBefore(bookEnd) && slotEnd.isAfter(bookStart)) {
                    isBooked = true;
                    break;
                }
            }
            slot.put("available", !isBooked);
            slot.put("past", date.equals(LocalDate.now()) && slotStart.isBefore(LocalTime.now()));
            slots.add(slot);
            slotStart = slotEnd;
        }
        return slots;
    }
}
