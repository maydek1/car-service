package com.software.modsen.carservice.controller

import com.software.modsen.carservice.dto.request.CarRequest
import com.software.modsen.carservice.dto.response.CarResponse
import com.software.modsen.carservice.dto.response.CarResponseSet
import com.software.modsen.carservice.service.CarService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.modelmapper.ModelMapper
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/cars")
class CarController(
        private val carService: CarService,
        private val modelMapper: ModelMapper
) {

    @Operation(summary = "Получить машину по ID")
    @ApiResponse(responseCode = "200", description = "Машина найдена",
            content = [Content(schema = Schema(implementation = CarResponse::class))])
    @ApiResponse(responseCode = "404", description = "Машина не найдена")
    @GetMapping("/{id}")
    fun getCarById(@PathVariable id: Long): ResponseEntity<CarResponse> {
        val car = carService.getCarById(id)
        return ResponseEntity.ok(modelMapper.map(car, CarResponse::class.java))
    }

    @Operation(summary = "Создать машину")
    @ApiResponse(responseCode = "201", description = "Машина успешно создана",
            content = [Content(schema = Schema(implementation = CarResponse::class))])
    @PostMapping
    fun createCar(@RequestBody carRequest: CarRequest): ResponseEntity<CarResponse> {
        val car = carService.createCar(carRequest)
        return ResponseEntity(modelMapper.map(car, CarResponse::class.java), HttpStatus.CREATED)
    }

    @Operation(summary = "Обновить информацию о машине")
    @ApiResponse(responseCode = "200", description = "Машина успешно обновлена",
            content = [Content(schema = Schema(implementation = CarResponse::class))])
    @ApiResponse(responseCode = "404", description = "Машина не найдена")
    @PutMapping("/{id}")
    fun updateCar(@PathVariable id: Long, @RequestBody carRequest: CarRequest): ResponseEntity<CarResponse> {
        val updatedCar = carService.updateCar(id, carRequest)
        return ResponseEntity.ok(modelMapper.map(updatedCar, CarResponse::class.java))
    }

    @Operation(summary = "Удалить машину по ID")
    @ApiResponse(responseCode = "200", description = "Машина успешно удалена",
            content = [Content(schema = Schema(implementation = CarResponse::class))])
    @ApiResponse(responseCode = "404", description = "Машина не найдена")
    @DeleteMapping("/{id}")
    fun deleteCarById(@PathVariable id: Long): ResponseEntity<CarResponse> {
        val deletedCar = carService.deleteCarById(id)
        return ResponseEntity.ok(modelMapper.map(deletedCar, CarResponse::class.java))
    }

    @Operation(summary = "Получить список всех машин")
    @ApiResponse(responseCode = "200", description = "Машины успешно получены",
            content = [Content(schema = Schema(implementation = CarResponseSet::class))])
    @GetMapping
    fun getAllCars(): ResponseEntity<CarResponseSet> {
        val cars = carService.getAllCars()
            .map { modelMapper.map(it, CarResponse::class.java) }
            .toSet()
        return ResponseEntity.ok(CarResponseSet(cars))
    }
}
