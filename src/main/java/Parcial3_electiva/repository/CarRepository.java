package Parcial3_electiva.repository;

import Parcial3_electiva.models.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {
}
