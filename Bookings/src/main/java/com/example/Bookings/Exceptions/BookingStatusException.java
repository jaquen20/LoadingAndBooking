package com.example.Bookings.Exceptions;

import org.springframework.web.bind.annotation.ExceptionHandler;

public class BookingStatusException extends RuntimeException {

    public BookingStatusException(String exceptionMessage){
        super(exceptionMessage);
    }
}
