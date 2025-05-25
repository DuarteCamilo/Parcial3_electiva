package Parcial3_electiva.controller;

import Parcial3_electiva.models.Car;
import Parcial3_electiva.services.CarCreateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/cars-create")
public class CarCreateController {

    @Autowired
    private CarCreateService carCreateService;

    @PostMapping
    public Car createCar(@RequestBody Car car) {
        return carCreateService.createCar(car);
    }
    
}
