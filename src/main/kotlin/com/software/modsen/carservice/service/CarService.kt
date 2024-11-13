package com.software.modsen.carservice.service

import com.software.modsen.carservice.dto.request.CarRequest
import com.software.modsen.carservice.exception.CarNotFoundException
import com.software.modsen.carservice.exception.CarNumberAlreadyExistException
import com.software.modsen.carservice.model.Car
import com.software.modsen.carservice.repository.CarRepository
import com.software.modsen.carservice.util.carNumberAlreadyExists
import com.software.modsen.carservice.util.carNotFound
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service

@Service
class CarService(
    private val carRepository: CarRepository,
    private val modelMapper: ModelMapper
) {

    suspend fun getCarById(id: Long): Car {
        return getOrElseThrow(id)
    }

    suspend fun deleteCarById(id: Long): Car {
        val car = getOrElseThrow(id)
        withContext(Dispatchers.IO) {
            carRepository.deleteById(id)
        }
        return car
    }

    suspend fun updateCar(id: Long, carRequest: CarRequest): Car {
        val car = withContext(Dispatchers.IO) {
            carRepository.findById(id).orElseThrow {
                CarNotFoundException(carNotFound(id))
            }
        }

        checkNumberToExist(car.number, carRequest.number)

        val updatedCar = modelMapper.map(carRequest, Car::class.java).apply {
            this.id = car.id
        }
        return withContext(Dispatchers.IO) {
            carRepository.save(updatedCar)
        }
    }

    suspend fun createCar(carRequest: CarRequest): Car {
        if (withContext(Dispatchers.IO) { carRepository.existsByNumber(carRequest.number) }) {
            throw CarNumberAlreadyExistException(carNumberAlreadyExists(carRequest.number))
        }

        val car = modelMapper.map(carRequest, Car::class.java)
        return withContext(Dispatchers.IO) {
            carRepository.save(car)
        }
    }

    private suspend fun getOrElseThrow(id: Long): Car {
        return withContext(Dispatchers.IO) {
            carRepository.findById(id).orElseThrow {
                CarNotFoundException(carNotFound(id))
            }
        }
    }

    suspend fun getAllCars(): List<Car> {
        return withContext(Dispatchers.IO) {
            carRepository.findAll()
        }
    }

    private suspend fun checkNumberToExist(currentNumber: String, newNumber: String) {
        if (currentNumber != newNumber && withContext(Dispatchers.IO) { carRepository.existsByNumber(newNumber) }) {
            throw CarNumberAlreadyExistException(carNumberAlreadyExists(newNumber))
        }
    }
}
