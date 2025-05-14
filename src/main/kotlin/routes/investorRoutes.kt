package com.jdulfer.routes

import com.jdulfer.services.InvestorService
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
            val commitments = investorService.getInvestorCommitments(investorId, null)
            call.respond(commitments)
        }
    }
}