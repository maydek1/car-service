package com.software.modsen.carservice.config

import org.modelmapper.ModelMapper
import org.modelmapper.convention.MatchingStrategies
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MapperConfig {
    @Bean
    fun modelMapper(): ModelMapper = ModelMapper().apply{configuration.matchingStrategy = MatchingStrategies.STRICT}
}