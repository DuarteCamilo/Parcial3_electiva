package Parcial3_electiva.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import Parcial3_electiva.models.Car;
import Parcial3_electiva.services.CarCreateService;
import Parcial3_electiva.services.CarDeleteService;
import Parcial3_electiva.services.CarGetAllService;
import Parcial3_electiva.services.CarGetByIdService;
import Parcial3_electiva.services.CarUpdateService;
import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class CarIntegrationTest {

    @Autowired
    private CarCreateService carCreateService;

    @Autowired
    private CarGetByIdService carGetByIdService;
    
    @Autowired
    private CarUpdateService carUpdateService;
    
    @Autowired
    private CarDeleteService carDeleteService;
    
    @Autowired
    private CarGetAllService carGetAllService;

    @Test
    void testCreateAndRetrieveCar() {
        // Crear un coche
        Car car = new Car();
        car.setBrand("Toyota");
        car.setModel("Corolla");
        car.setYear(2022);
        car.setColor("Rojo");
        car.setPrice(25000.0);
        
        Car savedCar = carCreateService.createCar(car);
        
        // Verificar que el coche se ha creado correctamente
        assertNotNull(savedCar.getId());
        assertEquals("Toyota", savedCar.getBrand());
        
        // Recuperar el coche por ID
        Optional<Car> retrievedCar = carGetByIdService.getCarById(savedCar.getId());
        assertTrue(retrievedCar.isPresent());
        assertEquals("Toyota", retrievedCar.get().getBrand());
    }

    @Test
    void testGetAllCars() {
        // Crear varios coches
        Car car1 = new Car();
        car1.setBrand("Toyota");
        car1.setModel("Corolla");
        car1.setYear(2022);
        car1.setColor("Rojo");
        car1.setPrice(25000.0);
        carCreateService.createCar(car1);
        
        Car car2 = new Car();
        car2.setBrand("Honda");
        car2.setModel("Civic");
        car2.setYear(2023);
        car2.setColor("Azul");
        car2.setPrice(27000.0);
        carCreateService.createCar(car2);
        
        // Obtener todos los coches
        List<Car> cars = carGetAllService.getAllCars();
        
        // Verificar que hay al menos dos coches
        assertTrue(cars.size() >= 2);
        
        // Verificar que los coches creados están en la lista
        boolean foundToyota = false;
        boolean foundHonda = false;
        
        for (Car car : cars) {
            if ("Toyota".equals(car.getBrand()) && "Corolla".equals(car.getModel())) {
                foundToyota = true;
            }
            if ("Honda".equals(car.getBrand()) && "Civic".equals(car.getModel())) {
                foundHonda = true;
            }
        }
        
        assertTrue(foundToyota);
        assertTrue(foundHonda);
    }

    @Test
    void testUpdateCar() {
        // Crear un coche
        Car car = new Car();
        car.setBrand("Toyota");
        car.setModel("Corolla");
        car.setYear(2022);
        car.setColor("Rojo");
        car.setPrice(25000.0);
        
        Car savedCar = carCreateService.createCar(car);
        
        // Actualizar el coche
        Car updatedDetails = new Car();
        updatedDetails.setBrand("Toyota");
        updatedDetails.setModel("Corolla");
        updatedDetails.setYear(2022);
        updatedDetails.setColor("Azul");
        updatedDetails.setPrice(27000.0);
        
        Optional<Car> updatedCar = carUpdateService.updateCar(savedCar.getId(), updatedDetails);
        
        // Verificar que el coche se ha actualizado correctamente
        assertTrue(updatedCar.isPresent());
        assertEquals("Azul", updatedCar.get().getColor());
        assertEquals(27000.0, updatedCar.get().getPrice());
        
        // Verificar que los cambios se han guardado en la base de datos
        Optional<Car> retrievedCar = carGetByIdService.getCarById(savedCar.getId());
        assertTrue(retrievedCar.isPresent());
        assertEquals("Azul", retrievedCar.get().getColor());
        assertEquals(27000.0, retrievedCar.get().getPrice());
    }

    @Test
    void testDeleteCar() {
        // Crear un coche
        Car car = new Car();
        car.setBrand("Toyota");
        car.setModel("Corolla");
        car.setYear(2022);
        car.setColor("Rojo");
        car.setPrice(25000.0);
        
        Car savedCar = carCreateService.createCar(car);
        
        // Verificar que el coche existe
        Optional<Car> retrievedCar = carGetByIdService.getCarById(savedCar.getId());
        assertTrue(retrievedCar.isPresent());
        
        // Eliminar el coche
        boolean deleted = carDeleteService.deleteCar(savedCar.getId());
        assertTrue(deleted);
        
        // Verificar que el coche ha sido eliminado
        Optional<Car> deletedCar = carGetByIdService.getCarById(savedCar.getId());
        assertFalse(deletedCar.isPresent());
    }
    
}
