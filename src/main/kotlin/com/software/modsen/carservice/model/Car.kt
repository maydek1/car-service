package com.software.modsen.carservice.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table(name = "car")
data class Car(
    @Id
    var id: Long,
    var number: String,
    var model: String,
    var color: String,
){
    constructor() : this(id = 0, number = "", model = "", color = "")
}

