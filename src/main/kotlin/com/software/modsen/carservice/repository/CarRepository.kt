package com.software.modsen.carservice.repository

import com.software.modsen.carservice.model.Car
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CarRepository : JpaRepository<Car,Long> {
    fun existsByNumber(number: String?): Boolean
}