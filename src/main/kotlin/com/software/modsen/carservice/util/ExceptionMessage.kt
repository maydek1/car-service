package com.software.modsen.carservice.util

fun carNotFound(id: Long): String {
    return "Not found car with id: '$id'"
}

fun carNumberAlreadyExists(number: String?): String {
    return "Car with number '$number' already exists"
}
