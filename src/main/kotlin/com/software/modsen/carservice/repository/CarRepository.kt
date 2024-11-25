package com.software.modsen.carservice.repository

import com.software.modsen.carservice.model.Car
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface CarRepository : ReactiveCrudRepository<Car,Long> {
    fun existsByNumber(number: String?): Mono<Boolean>
}