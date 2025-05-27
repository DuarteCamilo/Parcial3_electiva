package Parcial3_electiva.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import Parcial3_electiva.models.Car;
import Parcial3_electiva.repository.CarRepository;
import org.springframework.stereotype.Service;

@Service
public class CarUpdateService {
    @Autowired
    private CarRepository carRepository;
    
    public Optional<Car> updateCar(Long id, Car carDetails) {
        Optional<Car> carOptional = carRepository.findById(id);
        
        if (carOptional.isPresent()) {
            Car car = carOptional.get();
            car.setId(id);
            car.setModel(carDetails.getModel()); 
            car.setBrand(carDetails.getBrand()); 
            car.setYear(carDetails.getYear());   
            car.setPrice(carDetails.getPrice()); 
            car.setColor(carDetails.getColor()); 
            return Optional.of(carRepository.save(car));
        }
        
        return Optional.empty();
    }
}
