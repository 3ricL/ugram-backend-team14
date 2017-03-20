package ugram

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import ugram.config.Config
import ugram.database.initPostGre
import ugram.util.loadYml

@SpringBootApplication
open class Application {
    @Bean
    open fun init() = CommandLineRunner {
        val config = loadYml("config.yml", Config::class.java)
        initPostGre(config.database)
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}