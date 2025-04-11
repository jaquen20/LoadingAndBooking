package com.example.Bookings.Exceptions;

public class LoadNotFoundException extends RuntimeException{
    public LoadNotFoundException(String exceptionMessage){
        super(exceptionMessage);
    }
}
