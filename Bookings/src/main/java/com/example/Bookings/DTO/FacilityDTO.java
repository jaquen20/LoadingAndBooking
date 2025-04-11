package com.example.Bookings.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;
@Data
public class FacilityDTO {

    @NotBlank(message = "loading location required must")
    private String loadingPoint;
    @NotBlank(message = "unloading location is required must")
    private String unloadingPoint;
    @NotBlank(message = "put loading date")
    private Timestamp loadingDate;
    @NotBlank(message = "check unloading date")
    private Timestamp unloadingDate;
}
