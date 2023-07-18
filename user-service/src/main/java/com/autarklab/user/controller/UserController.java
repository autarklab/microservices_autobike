package com.autarklab.user.controller;

import com.autarklab.user.entity.User;
import com.autarklab.user.model.Bike;
import com.autarklab.user.model.Car;
import com.autarklab.user.service.UserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        List<User> users = userService.getAll();

        if (users.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(users);
    }

    @GetMapping("{id}")
    public ResponseEntity<User> getById(@PathVariable (value = "id") int id) {
        User userFound = userService.getUserById(id);

        if (userFound == null){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(userFound);
    }

    @PostMapping
    public ResponseEntity<User> save(@RequestBody User user) {
        return ResponseEntity.ok(userService.save(user));
    }

    @CircuitBreaker(name = "carsCB", fallbackMethod = "fallBackGetCars")
    @GetMapping("/cars/{userId}")
    public ResponseEntity<List<Car>> getCars(@PathVariable(name = "userId") int userId) {

        User user = userService.getUserById(userId);
        if(user == null){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(userService.getCars(userId));
    }

    @CircuitBreaker(name = "carsCB", fallbackMethod = "fallBackSaveCars")
    @PostMapping("/saveCar/{userId}")
    public ResponseEntity<Car> saveCar (@PathVariable(name = "userId") int userId, @RequestBody Car car){

        if(userService.getUserById(userId) == null){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(userService.saveCar(userId, car));
    }

    @CircuitBreaker(name = "bikesCB", fallbackMethod = "fallBackGetBikes")
    @GetMapping("/bikes/{userId}")
    public ResponseEntity<List<Bike>> getBikes(@PathVariable(name = "userId") int userId) {

        User user = userService.getUserById(userId);
        if(user == null){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(userService.getBikes(userId));
    }

    @CircuitBreaker(name = "bikesCB", fallbackMethod = "fallBackSaveBikes")
    @PostMapping("/saveBike/{userId}")
    public ResponseEntity<Bike> saveBike (@PathVariable(name = "userId") int userId, @RequestBody Bike bike){
        if(userService.getUserById(userId) == null){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(userService.saveBike(userId, bike));
    }

    @CircuitBreaker(name = "allCB", fallbackMethod = "fallBackGetAll")
    @GetMapping("/getAll/{userId}")
    public ResponseEntity<Map<String,Object>> getAllVehicles(@PathVariable(name = "userId") int userId) {
        return ResponseEntity.ok(userService.getAllVehiclesByUser(userId));
    }

    private ResponseEntity<List<Car>> fallBackGetCars(@PathVariable("userId") int userId, RuntimeException e) {
        return new ResponseEntity("El usuario: " + userId + " tiene los coches en el taller" , HttpStatus.OK);
    }

    private ResponseEntity<Car> fallBackSaveCars(@PathVariable("userId") int userId, @RequestBody Car car, RuntimeException e) {
        return new ResponseEntity("El usuario: " + userId + " no tiene dinero para carros" , HttpStatus.OK);
    }

    private ResponseEntity<List<Car>> fallBackGetBikes(@PathVariable("userId") int userId, RuntimeException e) {
        return new ResponseEntity("El usuario: " + userId + " tiene las motos en el taller" , HttpStatus.OK);
    }

    private ResponseEntity<Car> fallBackSaveBikes(@PathVariable("userId") int userId, @RequestBody Bike ike, RuntimeException e) {
        return new ResponseEntity("El usuario: " + userId + " no tiene dinero para motos" , HttpStatus.OK);
    }

    private ResponseEntity<Map<String, Object>> fallBackGetAll(@PathVariable("userId") int userId, RuntimeException e) {
        return new ResponseEntity("El usuario: " + userId + " no tiene vehiculos" , HttpStatus.OK);
    }
}
