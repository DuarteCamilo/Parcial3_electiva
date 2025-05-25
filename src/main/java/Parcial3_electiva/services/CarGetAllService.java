package Parcial3_electiva.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import Parcial3_electiva.models.Car;
import Parcial3_electiva.repository.CarRepository;
import org.springframework.stereotype.Service;

@Service
public class CarGetAllService {
    @Autowired
    private CarRepository carRepository;

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }
    
}
