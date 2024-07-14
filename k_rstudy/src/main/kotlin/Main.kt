package com.krstudy.kapi
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
//import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
//@EnableJpaAuditing
@EnableCaching
class Main
fun main(args: Array<String>) {
    runApplication<Main>(*args)
}
