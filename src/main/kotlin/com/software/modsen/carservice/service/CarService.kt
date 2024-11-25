package com.software.modsen.carservice.service

import com.software.modsen.carservice.dto.request.CarRequest
import com.software.modsen.carservice.exception.CarNotFoundException
import com.software.modsen.carservice.exception.CarNumberAlreadyExistException
import com.software.modsen.carservice.model.Car
import com.software.modsen.carservice.repository.CarRepository
import com.software.modsen.carservice.util.carNumberAlreadyExists
import com.software.modsen.carservice.util.carNotFound
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class CarService(
    private val carRepository: CarRepository,
    private val modelMapper: ModelMapper
) {

    fun getCarById(id: Long): Mono<Car> {
        return carRepository.findById(id)
            .switchIfEmpty(Mono.error(CarNotFoundException(carNotFound(id))))
    }

    fun deleteCarById(id: Long): Mono<Car> {
        return getCarById(id)
            .flatMap { car ->
                carRepository.deleteById(car.id).thenReturn(car)
            }
    }

    fun updateCar(id: Long, carRequest: CarRequest): Mono<Car> {
        return carRepository.findById(id)
            .switchIfEmpty(Mono.error(CarNotFoundException(carNotFound(id))))
            .flatMap { existingCar ->
                checkNumberToExist(existingCar.number, carRequest.number)
                    .then(
                        Mono.just(modelMapper.map(carRequest, Car::class.java).apply {
                            this.id = existingCar.id
                        })
                    )
            }
            .flatMap(carRepository::save)
    }

    fun createCar(carRequest: CarRequest): Mono<Car> {
        return carRepository.existsByNumber(carRequest.number)
            .flatMap { exists ->
                if (exists) {
                    Mono.error(CarNumberAlreadyExistException(carNumberAlreadyExists(carRequest.number)))
                } else {
                    val car = modelMapper.map(carRequest, Car::class.java)
                    carRepository.save(car)
                }
            }
    }

    fun getAllCars(): Flux<Car> {
        return carRepository.findAll()
    }

    private fun checkNumberToExist(currentNumber: String, newNumber: String): Mono<Void> {
        return if (currentNumber != newNumber) {
            carRepository.existsByNumber(newNumber)
                .flatMap { exists ->
                    if (exists) {
                        Mono.error(CarNumberAlreadyExistException(carNumberAlreadyExists(newNumber)))
                    } else {
                        Mono.empty()
                    }
                }
        } else {
            Mono.empty()
        }
    }
}
