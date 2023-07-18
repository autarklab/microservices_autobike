package com.autarklab.user.feignClients;

import com.autarklab.user.model.Bike;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "bike-service", path = "/api/bike")
public interface BikeFeignClient {

    @PostMapping()
    Bike save(Bike bike);

    @GetMapping("/byUser/{userId}")
    List<Bike> getBikes(@PathVariable(name = "userId") int userId);
}
