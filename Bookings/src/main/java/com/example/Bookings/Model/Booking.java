package com.example.Bookings.Model;

import com.example.Bookings.Enum.BookingStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Data @Entity
public class Booking {

    @Id
    private UUID id;
    @NotBlank(message = "load id required")
    private UUID loadId;
    @NotBlank(message = "transporter id required")
    private String transporterId;
    @DecimalMin(value = "10.00",message = "rate is require with min val of 10")
    private double proposedRate;
    @Size(max = 100,message ="not exceeds 100 characters" )
    private String comment;
    @NotBlank(message = "request time required")
    private Timestamp requestedAt;
    private BookingStatus bookingStatus;

}
