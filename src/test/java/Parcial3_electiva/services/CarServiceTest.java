package Parcial3_electiva.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
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

import Parcial3_electiva.models.Car;
import Parcial3_electiva.repository.CarRepository;


public class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarCreateService carCreateService;

    @InjectMocks
    private CarGetByIdService carGetByIdService;
    
    @InjectMocks
    private CarUpdateService carUpdateService;
    
    @InjectMocks
    private CarDeleteService carDeleteService;
    
    @InjectMocks
    private CarGetAllService carGetAllService;

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
        when(carRepository.findAll()).thenReturn(cars);

        // Act
        List<Car> result = carGetAllService.getAllCars();

        // Assert
        assertEquals(1, result.size());
        assertEquals("Toyota", result.get(0).getBrand());
        verify(carRepository, times(1)).findAll();
    }

    @Test
    void getCarById_WithExistingId_ShouldReturnCar() {
        // Arrange
        when(carRepository.findById(1L)).thenReturn(Optional.of(testCar));

        // Act
        Optional<Car> result = carGetByIdService.getCarById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Toyota", result.get().getBrand());
        verify(carRepository, times(1)).findById(1L);
    }

    @Test
    void getCarById_WithNonExistingId_ShouldReturnEmpty() {
        // Arrange
        when(carRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Optional<Car> result = carGetByIdService.getCarById(99L);

        // Assert
        assertFalse(result.isPresent());
        verify(carRepository, times(1)).findById(99L);
    }

    @Test
    void createCar_ShouldReturnCreatedCar() {
        // Arrange
        when(carRepository.save(any(Car.class))).thenReturn(testCar);

        // Act
        Car result = carCreateService.createCar(testCar);

        // Assert
        assertEquals("Toyota", result.getBrand());
        verify(carRepository, times(1)).save(any(Car.class));
    }

    @Test
    void updateCar_WithExistingId_ShouldReturnUpdatedCar() {
        // Arrange
        when(carRepository.findById(1L)).thenReturn(Optional.of(testCar));
        when(carRepository.save(any(Car.class))).thenReturn(testCar);

        Car updatedCar = new Car();
        updatedCar.setBrand("Honda");
        updatedCar.setModel("Civic");
        updatedCar.setYear(2023);
        updatedCar.setColor("Azul");
        updatedCar.setPrice(27000.0);

        // Act
        Optional<Car> result = carUpdateService.updateCar(1L, updatedCar);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Honda", result.get().getBrand());
        verify(carRepository, times(1)).findById(1L);
        verify(carRepository, times(1)).save(any(Car.class));
    }

    @Test
    void updateCar_WithNonExistingId_ShouldReturnEmpty() {
        // Arrange
        when(carRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Optional<Car> result = carUpdateService.updateCar(99L, testCar);

        // Assert
        assertFalse(result.isPresent());
        verify(carRepository, times(1)).findById(99L);
        verify(carRepository, never()).save(any(Car.class));
    }

    @Test
    void deleteCar_WithExistingId_ShouldReturnTrue() {
        // Arrange
        when(carRepository.existsById(1L)).thenReturn(true);
        doNothing().when(carRepository).deleteById(1L);

        // Act
        boolean result = carDeleteService.deleteCar(1L);

        // Assert
        assertTrue(result);
        verify(carRepository, times(1)).existsById(1L);
        verify(carRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteCar_WithNonExistingId_ShouldReturnFalse() {
        // Arrange
        when(carRepository.existsById(99L)).thenReturn(false);

        // Act
        boolean result = carDeleteService.deleteCar(99L);

        // Assert
        assertFalse(result);
        verify(carRepository, times(1)).existsById(99L);
        verify(carRepository, never()).deleteById(anyLong());
    }
    
}
