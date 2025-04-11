package com.example.Bookings.Controller;

import com.example.Bookings.DTO.BookingDTO;
import com.example.Bookings.Model.Booking;
import com.example.Bookings.Repository.LoadRepository;
import com.example.Bookings.Service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
@RestController @Validated
@RequestMapping("/booking")
public class BookingController {
    @Autowired
    private BookingService bookingService;
    @Autowired
    private LoadRepository loadRepository;

    @PostMapping
    public ResponseEntity<?>createBooking(@RequestBody BookingDTO bookingBody){
        Booking booking=bookingService.createNewBooking(bookingBody);
        return new ResponseEntity<>(booking, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getAllBookings(@RequestParam(required = false) String transporterId,
                                            @RequestParam(required = false) String shipperId){
        List<BookingDTO> bookingList;
        if (transporterId!=null && shipperId!=null){
            bookingList=bookingService.getBookingsByTransportIdAndShipperId(transporterId,shipperId);
        } else if (transporterId!=null ) {
            bookingList=bookingService.getBookingsByTransportationId(transporterId);
        }else if(shipperId!=null) {
            bookingList=bookingService.getBookingsByShipperId(shipperId);
        }else bookingList=bookingService.getAllBooking();
        return ResponseEntity.ok(bookingList);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<?> getBookingsById(@PathVariable UUID bookingId){
        BookingDTO bookingDTO=bookingService.getBookingById(bookingId);
        return ResponseEntity.ok(bookingDTO);
    }
    @PutMapping("/{bookingId}")
    public ResponseEntity<?> updateBooking(@PathVariable UUID bookingId,
                                           @RequestBody BookingDTO bookingDTO){
        BookingDTO updatedBooking=bookingService.updateBooking(bookingId,bookingDTO);
        return ResponseEntity.ok(updatedBooking);
    }
    @DeleteMapping("/{bookingId}")
    public ResponseEntity<?>deleteBookingById(@PathVariable UUID bookingId){
        bookingService.deleteBookingById(bookingId);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/accept/{bookingId}")
    public ResponseEntity<?>acceptBooking(@PathVariable UUID bookingId){
        BookingDTO booking=bookingService.acceptBooking(bookingId);
        return ResponseEntity.ok(booking);
    }

    @PostMapping("/reject/{bookingId}")
    public ResponseEntity<?>rejectBooking(@PathVariable UUID bookingId){
        BookingDTO booking=bookingService.rejectBooking(bookingId);
        return ResponseEntity.ok(booking);
    }

}
