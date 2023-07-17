package com.autarklab.carservice.controller;

import com.autarklab.carservice.entity.Car;
import com.autarklab.carservice.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/car")
public class CarController {

    private final CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public ResponseEntity<List<Car>> getAll() {
        List<Car> cars = carService.getAll();

        if (cars.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(cars);
    }

    @GetMapping("{id}")
    public ResponseEntity<Car> getById(@PathVariable (value = "id") int id) {
        Car carFound = carService.getCarById(id);

        if (carFound == null){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(carFound);
    }

    @GetMapping("/byUser/{userId}")
    public ResponseEntity<List<Car>> getByUserId(@PathVariable (value = "userId") int userId) {
        List<Car> cars = carService.carByUserId(userId);
        return ResponseEntity.ok(cars);
    }

    @PostMapping
    public ResponseEntity<Car> save(@RequestBody Car user) {
        return ResponseEntity.ok(carService.save(user));
    }
}
