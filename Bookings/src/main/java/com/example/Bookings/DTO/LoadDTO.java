package com.example.Bookings.DTO;

import com.example.Bookings.Enum.Status;
import com.example.Bookings.Model.Facility;
import jakarta.persistence.Embedded;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;
@Data
public class LoadDTO {
//    private UUID id;
//    private String shipperId;
//    private FacilityDTO facility;
//    private  String productType;
//    private String truckType;
//    private int noOfTrucks;
//    private double weight;
//    private String comment;
//    private Timestamp datePosted;
//    private Status status;priv
    private UUID id;
    @NotBlank(message = "shipper id is must")
    private String shipperId;
    @Embedded
    @NotBlank(message = "facility must not be blank")
    private FacilityDTO facility;
    @NotBlank(message = "product type required")
    private  String productType;
    @NotBlank(message = "truck type required")
    private String truckType;
    @Max(value = 40,message = "max 40 trucks are provided")
    @Min(value = 1,message = "select at least one trucks")
    private int noOfTrucks;
    @NotBlank(message = "weight should in tonn")
    private double weight;
    @Size(max = 100, message = "should not exceeds 100 characters")
    private String comment;
    private Timestamp datePosted;
    private Status status;
}
