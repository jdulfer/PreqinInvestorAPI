package com.jdulfer.routes

import com.jdulfer.services.InvestorService
import io.ktor.http.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.investorRoutes() {
    val investorService by application.inject<InvestorService>()

    route("/investors") {
        get {
            val investors = investorService.getInvestors()
            call.respond(investors)
        }

        get("/{investorId}/commitments") {
            val investorId = call.parameters["investorId"]
                ?: throw NotFoundException("Please provide an investor id")
            val commitments = try {
                investorService.getInvestorCommitments(investorId)
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, e.message ?: "Invalid investor id")
            } catch (e: NotFoundException) {
                call.respond(HttpStatusCode.NotFound, e.message ?: "Investor not found")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, e.message ?: "Internal server error")
            }
            call.respond(commitments)
        }
    }
}