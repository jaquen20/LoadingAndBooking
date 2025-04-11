package com.example.Bookings.Service;

import com.example.Bookings.DTO.FacilityDTO;
import com.example.Bookings.DTO.LoadDTO;
import com.example.Bookings.Enum.Status;
import com.example.Bookings.Exceptions.LoadNotFoundException;
import com.example.Bookings.Model.Load;
import com.example.Bookings.Repository.LoadRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class LoadService {
    @Autowired
    private LoadRepository loadRepository;

    public LoadDTO createNewLoad(Load load) {
        load.setId(UUID.randomUUID());
        load.setStatus(Status.POSTED);
        load.setDatePosted(Timestamp.from(Instant.now()));
        Load savedLoad= loadRepository.save(load);
        return convertToDTO(savedLoad);
    }
    public LoadDTO getLoadById(UUID loadId) {
        Load load=loadRepository.findById(loadId).orElseThrow(()-> new LoadNotFoundException("load with id "+loadId+" not found"));
        return convertToDTO(load);
    }

    public LoadDTO updateLoad(UUID loadId, Load loadBody) {
        Load existingLoad= loadRepository.findById(loadId).orElseThrow(()-> new LoadNotFoundException("load with id "+loadId+" not found"));;
        if (loadBody.getShipperId()!=null)existingLoad.setShipperId(loadBody.getShipperId());
        if (loadBody.getFacility()!=null)existingLoad.setFacility(loadBody.getFacility());
        if (loadBody.getProductType()!=null)existingLoad.setProductType(loadBody.getProductType());
        if (loadBody.getNoOfTrucks()!=0)existingLoad.setNoOfTrucks(loadBody.getNoOfTrucks());
        if (loadBody.getTruckType()!=null)existingLoad.setTruckType(loadBody.getTruckType());
        if (loadBody.getWeight()!=0)existingLoad.setWeight(loadBody.getWeight());
        if (loadBody.getComment()!=null)existingLoad.setComment(loadBody.getComment());
        if (loadBody.getStatus()!=null)existingLoad.setStatus(loadBody.getStatus());
        if (loadBody.getDatePosted()!=null)existingLoad.setDatePosted(loadBody.getDatePosted());
        loadRepository.save(existingLoad);
        return convertToDTO(existingLoad);
    }

    public void deleteLoad(UUID loadId) {
        Load load=loadRepository.findById(loadId).orElseThrow(()-> new LoadNotFoundException("load with id "+loadId+" not found"));
        loadRepository.delete(load);
    }
    public List<LoadDTO> searchLoads(String shipperId, String truckType, Status status) {
        System.out.println("truck types: "+truckType);
        if (shipperId==null && truckType==null && status==null){
            return loadRepository.findAll().stream()
                    .map(this::convertToDTO).collect(Collectors.toList());
        }
        Specification<Load> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (shipperId!=null) predicates.add(cb.equal(root.get("shipperId"), shipperId));
            if (truckType!=null) predicates.add(cb.equal(root.get("truckType"),truckType));
            if (status!=null) predicates.add(cb.equal(root.get("status"),status));
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return loadRepository.findAll(spec).stream().map(this::convertToDTO).collect(Collectors.toList());
    }



    private LoadDTO convertToDTO(Load load){
        FacilityDTO facilityDTO=new FacilityDTO();
        facilityDTO.setLoadingDate(load.getFacility().getLoadingDate());
        facilityDTO.setLoadingPoint(load.getFacility().getLoadingPoint());
        facilityDTO.setUnloadingDate(load.getFacility().getUnloadingDate());
        facilityDTO.setUnloadingPoint(load.getFacility().getUnloadingPoint());
        return getLoadDTO(load, facilityDTO);
    }

    private static LoadDTO getLoadDTO(Load load, FacilityDTO facilityDTO) {
        LoadDTO loadDTO=new LoadDTO();
        loadDTO.setId(load.getId());
        loadDTO.setWeight(load.getWeight());
        loadDTO.setComment(load.getComment());
        loadDTO.setNoOfTrucks(load.getNoOfTrucks());
        loadDTO.setProductType(load.getProductType());
        loadDTO.setShipperId(load.getShipperId());
        loadDTO.setTruckType(load.getTruckType());
        loadDTO.setDatePosted(load.getDatePosted());
        loadDTO.setFacility(facilityDTO);
        loadDTO.setStatus(load.getStatus());
        return loadDTO;
    }
}
