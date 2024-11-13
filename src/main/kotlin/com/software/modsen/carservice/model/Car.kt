package com.software.modsen.carservice.model

import jakarta.persistence.*

@Entity
@Table(name = "car")
data class Car(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,
    var number: String,
    var model: String,
    var color: String,
){
    constructor() : this(id = 0, number = "", model = "", color = "")
}

