package com.groupwork.booking_system.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ModelAndView handleNotFound(ResourceNotFoundException ex, WebRequest request) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("error", "Resource Not Found");
        mav.addObject("message", ex.getMessage());
        mav.addObject("status", 404);
        mav.setStatus(HttpStatus.NOT_FOUND);
        return mav;
    }

    @ExceptionHandler(BookingConflictException.class)
    public ModelAndView handleConflict(BookingConflictException ex, WebRequest request) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("error", "Booking Conflict");
        mav.addObject("message", ex.getMessage());
        mav.addObject("status", 409);
        mav.setStatus(HttpStatus.CONFLICT);
        return mav;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleGeneric(Exception ex, WebRequest request) {
        ex.printStackTrace();
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("error", "Internal Server Error");
        mav.addObject("message", ex.getMessage() != null ? ex.getMessage() : "An unexpected error occurred");
        mav.addObject("status", 500);
        mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        return mav;
    }
}