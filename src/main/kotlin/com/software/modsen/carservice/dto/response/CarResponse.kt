package com.software.modsen.carservice.dto.response

data class CarResponse(
        var id:Long,
        var number: String,
        var model: String,
        var color: String
){
        constructor() : this(id = 0, number = "", model = "", color = "")
}