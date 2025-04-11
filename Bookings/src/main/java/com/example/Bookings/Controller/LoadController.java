package com.example.Bookings.Controller;

import com.example.Bookings.DTO.LoadDTO;
import com.example.Bookings.Enum.Status;
import com.example.Bookings.Model.Load;
import com.example.Bookings.Service.LoadService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController @Validated
@RequestMapping("/load")
public class LoadController {
    @Autowired
    private LoadService loadService;

    @PostMapping
    public ResponseEntity<?> createNewLoad(@Valid @RequestBody Load load){
        LoadDTO savedLoad=loadService.createNewLoad(load);
        return new ResponseEntity<>(savedLoad, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getFilteredLoads(@RequestParam(required = false) String shipperId,
                                              @RequestParam(required = false) String truckType,
                                              @RequestParam(required = false) Status status){
        List<LoadDTO> results=loadService.searchLoads(shipperId, truckType, status);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/{loadId}")
    public ResponseEntity<?> getLoadDetailsById(@PathVariable UUID loadId){
        LoadDTO fetchedLoad=loadService.getLoadById(loadId);
        return new ResponseEntity<>(fetchedLoad,HttpStatus.FOUND);

    }
    @PutMapping("/{loadId}")
    public ResponseEntity<?> updateLoad(@PathVariable UUID loadId, @RequestBody Load loadBody){
        LoadDTO updatedLoad=loadService.updateLoad(loadId,loadBody);
        return new ResponseEntity<>(updatedLoad,HttpStatus.OK);
    }

    @DeleteMapping("/{loadId}")
    public ResponseEntity<?> deleteLoad(@PathVariable UUID loadId){
        loadService.deleteLoad(loadId);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
