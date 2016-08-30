package com.djulia

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class AdtKotlinApplication

fun main(args: Array<String>) {
    SpringApplication.run(AdtKotlinApplication::class.java, *args)
}
