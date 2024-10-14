package com.software.modsen.carservice.model

import jakarta.persistence.*

@Entity
@Table(name = "car")
data class Car(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var number: String? = null,
    var model: String? = null,
    var color: String? = null,
)