package com.software.modsen.carservice.dto.request

data class CarRequest(
        var model: String,
        var number: String,
        var color: String
){
        constructor() : this(number = "", model = "", color = "")
}

