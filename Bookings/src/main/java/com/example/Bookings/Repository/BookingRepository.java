package com.example.Bookings.Repository;

import com.example.Bookings.DTO.BookingDTO;
import com.example.Bookings.Model.Booking;
import com.example.Bookings.Model.Load;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID>, JpaSpecificationExecutor<Booking> {
    List<Booking> findByTransporterId(String transporterId);

//    List<Booking> findByLoadId_ShipperId(String shipperId);
//
//    List<Booking> findByTransporterIdAndLoadId_ShipperId(String transporterId, String shipperId);

    Booking findByLoadId(UUID id);
}
