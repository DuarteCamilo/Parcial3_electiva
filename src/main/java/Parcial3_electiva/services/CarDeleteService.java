package Parcial3_electiva.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import Parcial3_electiva.repository.CarRepository;

@Service
public class CarDeleteService {
    @Autowired
    private CarRepository carRepository;

    public boolean deleteCar(Long id) {
        if (carRepository.existsById(id)) {
            carRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
