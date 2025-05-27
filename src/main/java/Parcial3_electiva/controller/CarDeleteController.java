package Parcial3_electiva.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import Parcial3_electiva.services.CarDeleteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;    

@RestController
@RequestMapping("/api/cars-delete")
public class CarDeleteController {
    @Autowired
    private CarDeleteService carDeleteService;

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        return carDeleteService.deleteCar(id) 
                ? ResponseEntity.ok().build() 
                : ResponseEntity.notFound().build();
    }
}
