package Parcial3_electiva.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Parcial3_electiva.models.Car;
import Parcial3_electiva.services.CarGetByIdService;

@RestController
@RequestMapping("/api/cars-get-by-id")
public class CarGetByIdController {

    @Autowired
    private CarGetByIdService carGetByIdService;   

    @GetMapping("/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable Long id) {
        return carGetByIdService.getCarById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
