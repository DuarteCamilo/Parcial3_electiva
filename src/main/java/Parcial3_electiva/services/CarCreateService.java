package Parcial3_electiva.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import Parcial3_electiva.models.Car;
import Parcial3_electiva.repository.CarRepository;

@Service
public class CarCreateService {
    @Autowired
    private CarRepository carRepository;

    public Car createCar(Car car) {
        return carRepository.save(car);
    }  
}