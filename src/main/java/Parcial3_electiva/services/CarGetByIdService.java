package Parcial3_electiva.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import Parcial3_electiva.models.Car;
import Parcial3_electiva.repository.CarRepository;
import org.springframework.stereotype.Service;

@Service
public class CarGetByIdService {
    @Autowired
    private CarRepository carRepository;
    
    public Optional<Car> getCarById(Long id) {
        return carRepository.findById(id);
    } 
}
