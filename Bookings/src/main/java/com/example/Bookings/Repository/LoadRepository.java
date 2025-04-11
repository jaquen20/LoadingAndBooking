package com.example.Bookings.Repository;

import com.example.Bookings.Model.Load;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface LoadRepository extends JpaRepository<Load, UUID>, JpaSpecificationExecutor<Load> {
    List<Load> findByShipperId(String shipperId);
}
