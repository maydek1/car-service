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
import org.slf4j.LoggerFactory

@Service
class CarService(
    private val carRepository: CarRepository,
    private val modelMapper: ModelMapper
) {
    private val logger = LoggerFactory.getLogger(CarService::class.java)

    fun getCarById(id: Long): Car {
        logger.info("Запрос на получение автомобиля с ID: $id")
        return getOrElseThrow(id)
    }

    fun deleteCarById(id: Long): Car {
        val car = getOrElseThrow(id)
        logger.info("Удаление автомобиля с ID: $id")
        carRepository.deleteById(id)
        return car
    }

    fun updateCar(id: Long, carRequest: CarRequest): Car {
        logger.info("Обновление автомобиля с ID: $id")
        val car = carRepository.findById(id).orElseThrow {
            CarNotFoundException(carNotFound(id))
        }

        checkNumberToExist(car.number, carRequest.number)
        logger.info("Новый номер автомобиля: ${carRequest.number}")

        val updatedCar = modelMapper.map(carRequest, Car::class.java).apply {
            this.id = car.id
        }
        return carRepository.save(updatedCar)
    }

    fun createCar(carRequest: CarRequest): Car {
        logger.info("Создание нового автомобиля с номером: ${carRequest.number}")
        if (carRepository.existsByNumber(carRequest.number)) {
            logger.error("Номер автомобиля уже существует: ${carRequest.number}")
            throw CarNumberAlreadyExistException(carNumberAlreadyExists(carRequest.number))
        }

        val car = modelMapper.map(carRequest, Car::class.java)
        return carRepository.save(car)
    }

    fun getAllCars(): List<Car> {
        logger.info("Получение списка всех автомобилей")
        return carRepository.findAll()
    }

    private fun getOrElseThrow(id: Long): Car {
        return carRepository.findById(id).orElseThrow {
            CarNotFoundException(carNotFound(id))
        }
    }

    private fun checkNumberToExist(currentNumber: String, newNumber: String) {
        if (currentNumber != newNumber && carRepository.existsByNumber(newNumber)) {
            throw CarNumberAlreadyExistException(carNumberAlreadyExists(newNumber))
        }
    }
}
