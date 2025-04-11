package com.example.Bookings.Model;

import com.example.Bookings.Enum.Status;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;
@Data @Entity @Table(name = "load_table")
public class Load {

    @Id
    private UUID id;
    @NotBlank(message = "shipper id is must")
    private String shipperId;
    @Valid @Embedded @NotNull
    private Facility facility;
    @NotBlank(message = "product type required")
    private  String productType;
    @NotBlank(message = "truck type required")
    private String truckType;
    @Max(value = 40,message = "max 40 trucks are provided")
    @Min(value = 1,message = "select at least one trucks")
    private int noOfTrucks;

    @DecimalMin(value = "1.00", message = "minimum 1 tonn")
    private double weight;
    @Size(max = 100, message = "should not exceeds 100 characters")
    private String comment;
    @PastOrPresent
    private Timestamp datePosted;
    private Status status;

}
