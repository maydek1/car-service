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

@Service
class CarService(
        private val carRepository: CarRepository,
        private val modelMapper: ModelMapper
) {

    fun getCarById(id: Long): Car {
        return getOrElseThrow(id)
    }

    fun deleteCarById(id: Long): Car {
        val car = getOrElseThrow(id)
        carRepository.deleteById(id)
        return car
    }

    fun updateCar(id: Long, carRequest: CarRequest): Car {
        val car = carRepository.findById(id).orElseThrow {
            CarNotFoundException(carNotFound(id))
        }

        checkNumberToExist(car.number, carRequest.number)

        val updatedCar = modelMapper.map(carRequest, Car::class.java).apply {
            this.id = car.id
        }
        return carRepository.save(updatedCar)
    }

    fun createCar(carRequest: CarRequest): Car {
        if (carRepository.existsByNumber(carRequest.number)) {
            throw CarNumberAlreadyExistException(carNumberAlreadyExists(carRequest.number))
        }

        val car = modelMapper.map(carRequest, Car::class.java)
        return carRepository.save(car)
    }

    private fun getOrElseThrow(id: Long): Car {
        return carRepository.findById(id).orElseThrow {
            CarNotFoundException(carNotFound(id))
        }
    }

    fun getAllCars(): List<Car> {
        return carRepository.findAll()
    }

    private fun checkNumberToExist(currentNumber: String, newNumber: String) {
        if (currentNumber != newNumber && carRepository.existsByNumber(newNumber)) {
            throw CarNumberAlreadyExistException(carNumberAlreadyExists(newNumber))
        }
    }
}
