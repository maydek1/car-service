package com.software.modsen.carservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@SpringBootApplication
@EnableDiscoveryClient
class CarServiceApplication

fun main(args: Array<String>) {
	runApplication<CarServiceApplication>(*args)
}
