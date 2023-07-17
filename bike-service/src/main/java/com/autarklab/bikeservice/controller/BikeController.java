package com.autarklab.bikeservice.controller;

import com.autarklab.bikeservice.entity.Bike;
import com.autarklab.bikeservice.service.BikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bike")
public class BikeController {

    private final BikeService bikeService;

    @Autowired
    public BikeController(BikeService bikeService) {
        this.bikeService = bikeService;
    }

    @GetMapping
    public ResponseEntity<List<Bike>> getAll() {
        List<Bike> cars = bikeService.getAll();

        if (cars.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(cars);
    }

    @GetMapping("{id}")
    public ResponseEntity<Bike> getById(@PathVariable (value = "id") int id) {
        Bike bikeFound = bikeService.getBikeById(id);

        if (bikeFound == null){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(bikeFound);
    }

    @GetMapping("/byUser/{userId}")
    public ResponseEntity<List<Bike>> getByUserId(@PathVariable (value = "userId") int userId) {
        List<Bike> cars = bikeService.bikeByUserId(userId);
        return ResponseEntity.ok(cars);
    }

    @PostMapping
    public ResponseEntity<Bike> save(@RequestBody Bike user) {
        return ResponseEntity.ok(bikeService.save(user));
    }
}
