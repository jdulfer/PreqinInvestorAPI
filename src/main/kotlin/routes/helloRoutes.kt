package com.jdulfer.routes

import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

fun Route.helloRoutes() {
    get("/hello") {
        call.respond("Hello, World!")
    }
}
