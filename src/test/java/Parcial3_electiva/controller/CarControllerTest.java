package Parcial3_electiva.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import Parcial3_electiva.models.Car;
import Parcial3_electiva.services.CarCreateService;
import Parcial3_electiva.services.CarDeleteService;
import Parcial3_electiva.services.CarGetAllService;
import Parcial3_electiva.services.CarGetByIdService;
import Parcial3_electiva.services.CarUpdateService;

public class CarControllerTest {

    @Mock
    private CarCreateService carCreateService;

    @Mock
    private CarGetByIdService carGetByIdService;
    
    @Mock
    private CarUpdateService carUpdateService;
    
    @Mock
    private CarDeleteService carDeleteService;
    
    @Mock
    private CarGetAllService carGetAllService;

    @InjectMocks
    private CarCreateController carCreateController;

    @InjectMocks
    private CarGetByIdController carGetByIdController;

    @InjectMocks
    private CarUpdateController carUpdateController;    
    
    @InjectMocks
    private CarDeleteController carDeleteController;

    @InjectMocks
    private CarGetAllController carGetAllController;

    private Car testCar;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        testCar = new Car();
        testCar.setId(1L);
        testCar.setBrand("Toyota");
        testCar.setModel("Corolla");
        testCar.setYear(2022);
        testCar.setColor("Rojo");
        testCar.setPrice(25000.0);
    }

    @Test
    void getAllCars_ShouldReturnAllCars() {
        // Arrange
        List<Car> cars = Arrays.asList(testCar);
        when(carGetAllService.getAllCars()).thenReturn(cars);

        // Act
        List<Car> result = carGetAllController.getAllCars();

        // Assert
        assertEquals(1, result.size());
        assertEquals("Toyota", result.get(0).getBrand());
        verify(carGetAllService, times(1)).getAllCars();
    }

    @Test
    void getCarById_WithExistingId_ShouldReturnCar() {
        // Arrange
        when(carGetByIdService.getCarById(1L)).thenReturn(Optional.of(testCar));

        // Act
        ResponseEntity<Car> response = carGetByIdController.getCarById(1L);

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals("Toyota", response.getBody().getBrand());
        verify(carGetByIdService, times(1)).getCarById(1L);
    }

    @Test
    void getCarById_WithNonExistingId_ShouldReturnNotFound() {
        // Arrange
        when(carGetByIdService.getCarById(99L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Car> response = carGetByIdController.getCarById(99L);

        // Assert
        assertTrue(response.getStatusCode().is4xxClientError());
        verify(carGetByIdService, times(1)).getCarById(99L);
    }

    @Test
    void createCar_ShouldReturnCreatedCar() {
        // Arrange
        when(carCreateService.createCar(any(Car.class))).thenReturn(testCar);

        // Act
        Car result = carCreateController.createCar(testCar);

        // Assert
        assertEquals("Toyota", result.getBrand());
        verify(carCreateService, times(1)).createCar(any(Car.class));
    }

    @Test
    void updateCar_WithExistingId_ShouldReturnUpdatedCar() {
        // Arrange
        when(carUpdateService.updateCar(eq(1L), any(Car.class))).thenReturn(Optional.of(testCar));

        // Act
        ResponseEntity<Car> response = carUpdateController.updateCar(1L, testCar);

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals("Toyota", response.getBody().getBrand());
        verify(carUpdateService, times(1)).updateCar(eq(1L), any(Car.class));
    }

    @Test
    void updateCar_WithNonExistingId_ShouldReturnNotFound() {
        // Arrange
        when(carUpdateService.updateCar(eq(99L), any(Car.class))).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Car> response = carUpdateController.updateCar(99L, testCar);

        // Assert
        assertTrue(response.getStatusCode().is4xxClientError());
        verify(carUpdateService, times(1)).updateCar(eq(99L), any(Car.class));
    }

    @Test
    void deleteCar_WithExistingId_ShouldReturnOk() {
        // Arrange
        when(carDeleteService.deleteCar(1L)).thenReturn(true);

        // Act
        ResponseEntity<Void> response = carDeleteController.deleteCar(1L);

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        verify(carDeleteService, times(1)).deleteCar(1L);
    }

    @Test
    void deleteCar_WithNonExistingId_ShouldReturnNotFound() {
        // Arrange
        when(carDeleteService.deleteCar(99L)).thenReturn(false);

        // Act
        ResponseEntity<Void> response = carDeleteController.deleteCar(99L);

        // Assert
        assertTrue(response.getStatusCode().is4xxClientError());
        verify(carDeleteService, times(1)).deleteCar(99L);
    }
    
}
