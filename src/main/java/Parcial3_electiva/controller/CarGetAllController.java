package Parcial3_electiva.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import Parcial3_electiva.models.Car;
import Parcial3_electiva.services.CarGetAllService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@RestController
@RequestMapping("/api/cars-get-all")
public class CarGetAllController {
    @Autowired
    private CarGetAllService carGetAllService;

    @GetMapping
    public List<Car> getAllCars() {
        return carGetAllService.getAllCars();
    }
    
}
