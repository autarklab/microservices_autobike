package com.autarklab.user.controller;

import com.autarklab.user.entity.User;
import com.autarklab.user.model.Bike;
import com.autarklab.user.model.Car;
import com.autarklab.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/cars/{userId}")
    public ResponseEntity<List<Car>> getCars(@PathVariable(name = "userId") int userId) {

        User user = userService.getUserById(userId);
        if(user == null){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(userService.getCars(userId));
    }

    @GetMapping("/bikes/{userId}")
    public ResponseEntity<List<Bike>> getBikes(@PathVariable(name = "userId") int userId) {

        User user = userService.getUserById(userId);
        if(user == null){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(userService.getBikes(userId));
    }

    @PostMapping("/saveCar/{userId}")
    public ResponseEntity<Car> saveCar (@PathVariable(name = "userId") int userId, @RequestBody Car car){

        if(userService.getUserById(userId) == null){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(userService.saveCar(userId, car));
    }

    @PostMapping("/saveBike/{userId}")
    public ResponseEntity<Bike> saveBike (@PathVariable(name = "userId") int userId, @RequestBody Bike bike){
        if(userService.getUserById(userId) == null){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(userService.saveBike(userId, bike));
    }

    @GetMapping("/getAll/{userId}")
    public ResponseEntity<Map<String,Object>> getAllVehicles(@PathVariable(name = "userId") int userId) {
        return ResponseEntity.ok(userService.getAllvehiclesByUser(userId));
    }
}
