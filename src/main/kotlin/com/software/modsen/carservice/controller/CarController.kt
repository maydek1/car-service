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
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/cars")
class CarController(
    private val carService: CarService,
    private val modelMapper: ModelMapper
) {

    @Operation(summary = "Получить машину по ID")
    @ApiResponse(
        responseCode = "200", description = "Машина найдена",
        content = [Content(schema = Schema(implementation = CarResponse::class))]
    )
    @ApiResponse(responseCode = "404", description = "Машина не найдена")
    @GetMapping("/{id}")
    fun getCarById(@PathVariable id: Long): Mono<ResponseEntity<CarResponse>> {
        return carService.getCarById(id)
            .map { car -> ResponseEntity.ok(modelMapper.map(car, CarResponse::class.java)) }
    }

    @Operation(summary = "Создать машину")
    @ApiResponse(
        responseCode = "201", description = "Машина успешно создана",
        content = [Content(schema = Schema(implementation = CarResponse::class))]
    )
    @PostMapping
    fun createCar(@RequestBody carRequest: CarRequest): Mono<ResponseEntity<CarResponse>> {
        return carService.createCar(carRequest)
            .map { car -> ResponseEntity(modelMapper.map(car, CarResponse::class.java), HttpStatus.CREATED) }
    }

    @Operation(summary = "Обновить информацию о машине")
    @ApiResponse(
        responseCode = "200", description = "Машина успешно обновлена",
        content = [Content(schema = Schema(implementation = CarResponse::class))]
    )
    @ApiResponse(responseCode = "404", description = "Машина не найдена")
    @PutMapping("/{id}")
    fun updateCar(
        @PathVariable id: Long,
        @RequestBody carRequest: CarRequest
    ): Mono<ResponseEntity<CarResponse>> {
        return carService.updateCar(id, carRequest)
            .map { car -> ResponseEntity.ok(modelMapper.map(car, CarResponse::class.java)) }
    }

    @Operation(summary = "Удалить машину по ID")
    @ApiResponse(
        responseCode = "200", description = "Машина успешно удалена",
        content = [Content(schema = Schema(implementation = CarResponse::class))]
    )
    @ApiResponse(responseCode = "404", description = "Машина не найдена")
    @DeleteMapping("/{id}")
    fun deleteCarById(@PathVariable id: Long): Mono<ResponseEntity<CarResponse>> {
        return carService.deleteCarById(id)
            .map { car -> ResponseEntity.ok(modelMapper.map(car, CarResponse::class.java)) }
    }

    @Operation(summary = "Получить список всех машин")
    @ApiResponse(
        responseCode = "200", description = "Машины успешно получены",
        content = [Content(schema = Schema(implementation = CarResponseSet::class))]
    )
    @GetMapping
    fun getAllCars(): Mono<ResponseEntity<CarResponseSet>> {
        return carService.getAllCars()
            .map { car -> modelMapper.map(car, CarResponse::class.java) }
            .collectList()
            .map { carResponses -> ResponseEntity.ok(CarResponseSet(carResponses.toSet())) }
    }
}
