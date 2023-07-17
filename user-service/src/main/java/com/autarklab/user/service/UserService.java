package com.autarklab.user.service;

import com.autarklab.user.entity.User;
import com.autarklab.user.feignClients.BikeFeignClient;
import com.autarklab.user.feignClients.CarFeignClient;
import com.autarklab.user.model.Bike;
import com.autarklab.user.model.Car;
import com.autarklab.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final CarFeignClient carFeignClient;
    private final BikeFeignClient bikeFeignClient;

    @Autowired
    public UserService(UserRepository userRepository,
                       RestTemplate restTemplate,
                       CarFeignClient carFeignClient,
                       BikeFeignClient bikeFeignClient) {
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
        this.carFeignClient = carFeignClient;
        this.bikeFeignClient = bikeFeignClient;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getUserById(int id) {
        return userRepository.findById(id).orElse(null);
    }

    public User save(User user){
        return userRepository.save(user);
    }

    public List<Car> getCars(int userId){
        List<Car> cars = restTemplate.getForObject("http://localhost:8002/api/car/byUser/" + userId, List.class);
        return cars;
    }

    public List<Bike> getBikes(int userId){
        List<Bike> bikes = restTemplate.getForObject("http://localhost:8003/api/bike/byUser/" + userId, List.class);
        return bikes;
    }

    public Car saveCar(int userId, Car car){
        car.setUserId(userId);
        return carFeignClient.save(car);
    }

    public Bike saveBike(int userId, Bike bike) {
        bike.setUserId(userId);
        return bikeFeignClient.save(bike);
    }

    public Map<String, Object> getAllvehiclesByUser(int id) {

        Map<String,Object> result = new HashMap<>();
        User user = userRepository.findById(id).orElse(null);
        if(user == null){
            result.put("Mensaje", "No existe el usuario");
            return result;
        }

        result.put("User", user);

        List<Car> cars = carFeignClient.getCars(id);
        if (cars.isEmpty()){
            result.put("Cars", "User has no cars");
        } else {
            result.put("Cars", cars);
        }

        List<Bike> bikes = bikeFeignClient.getBikes(id);
        if (bikes.isEmpty()){
            result.put("Bikes", "User has no bikes");
        } else {
            result.put("Bikes", bikes);
        }

        return result;
    }
}
