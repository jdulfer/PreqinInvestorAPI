package com.jdulfer.config

import com.jdulfer.di.appModule
import com.jdulfer.routes.helloRoutes
import com.jdulfer.routes.investorRoutes
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.Database
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.module() {
    install(Koin) {
        slf4jLogger()
        modules(
            listOf(
                appModule
            )
        )
    }

    install(CallLogging)

    install(CORS) {
        anyHost()
    }

    install(ContentNegotiation) {
        json()
    }

    routing {
        helloRoutes()
        investorRoutes()
    }

    Database.connect("jdbc:sqlite:src/main/resources/PreqinDB.db", "org.sqlite.JDBC")
}