package com.autarklab.bikeservice.service;

import com.autarklab.bikeservice.entity.Bike;
import com.autarklab.bikeservice.repository.BikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BikeService {

    private final BikeRepository bikeRepository;

    @Autowired
    public BikeService(BikeRepository bikeRepository) {
        this.bikeRepository = bikeRepository;
    }

    public List<Bike> getAll() {
        return bikeRepository.findAll();
    }

    public Bike getBikeById(int id) {
        return bikeRepository.findById(id).orElse(null);
    }

    public Bike save(Bike car){
        return bikeRepository.save(car);
    }

    public List<Bike> bikeByUserId (int userId) {
        return bikeRepository.findByUserId(userId);
    }
}
