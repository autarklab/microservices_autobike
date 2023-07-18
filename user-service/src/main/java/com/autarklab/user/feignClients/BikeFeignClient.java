package com.autarklab.user.feignClients;

import com.autarklab.user.model.Bike;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
// apartir de Spring 3.x se obvia en estas interfaces el @Request Body
// ya que  la comunicacion es diferente a la json/xml que maneja la etiqueta
// import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "bike-service", path = "/api/bike")
public interface BikeFeignClient {

    @PostMapping()
    Bike save(Bike bike);

    @GetMapping("/byUser/{userId}")
    List<Bike> getBikes(@PathVariable(name = "userId") int userId);
}
