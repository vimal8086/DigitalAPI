package com.one.digitalapi.controller;

import com.one.digitalapi.entity.BookingDetails;
import com.one.digitalapi.service.BookingService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/booking")
public class BookingController {

    private final BookingService bookingService;  // Inject BookingService

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/bookings/intermediate-seat")
    public String bookIntermediateSeat(@RequestBody BookingDetails bookingDetails) {
        bookingService.cacheBooking(bookingDetails);  //Use BookingService to store cache
        return "Seats booked successfully and will be held for 15 minutes!";
    }

    @GetMapping("/get-booking/{busId}/{date}")
    public BookingDetails getBooking(@PathVariable String busId, @PathVariable String date) {
        return bookingService.getCachedBooking(busId, date);  // Fetch from service
    }
}