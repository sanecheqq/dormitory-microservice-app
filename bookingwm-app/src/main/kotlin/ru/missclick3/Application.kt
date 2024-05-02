package ru.missclick3

import com.orbitz.consul.Consul
import com.orbitz.consul.model.agent.ImmutableRegistration
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.cors.routing.*
import ru.missclick3.config.DatabaseSingleton
import ru.missclick3.external.UserServiceImpl
import ru.missclick3.plugins.*
import ru.missclick3.repositories.TimeRangeRepositoryImpl
import ru.missclick3.repositories.WashingMachineRepositoryImpl
import ru.missclick3.services.TimeRangeServiceImpl
import ru.missclick3.services.WashingMachineServiceImpl

fun main() {
    val server = embeddedServer(Netty, port = 8086, host = "0.0.0.0", module = Application::module)
    val consulClient = Consul.builder().withUrl("http://localhost:8500").build()
    val service = ImmutableRegistration.builder()
        .id("booking-${server.environment.connectors[0].port}")
        .name("booking-app")
        .address("localhost")
        .port(server.environment.connectors[0].port)
        .build()
    consulClient.agentClient().register(service)
    server.start(wait = true)
}

fun Application.module() {
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Patch)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
        anyHost()
        allowCredentials = true
    }
    DatabaseSingleton.init()
    val userService = UserServiceImpl()

    val timeRangeRepository = TimeRangeRepositoryImpl()
    val timeRangeService = TimeRangeServiceImpl(timeRangeRepository)

    val washingMachineRepository = WashingMachineRepositoryImpl()
    val washingMachineService = WashingMachineServiceImpl(washingMachineRepository)

    configureSecurity()
    configureSerialization()
    configureRouting(
        userService,
        timeRangeService,
        washingMachineService
    )
}
