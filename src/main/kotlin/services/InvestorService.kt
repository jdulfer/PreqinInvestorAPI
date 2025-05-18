package com.jdulfer.services

import com.jdulfer.dao.InvestorDAO
import com.jdulfer.domain.Commitment
import com.jdulfer.domain.Investor

class InvestorService(
    private val investorDAO: InvestorDAO
) {
    suspend fun getInvestors(): List<Investor> = investorDAO.getInvestors()

    suspend fun getInvestorCommitments(investorId: String): List<Commitment> {
        val investorInt = try {
            investorId.toInt()
        } catch (e: NumberFormatException) {
            throw IllegalArgumentException("Investor id must be a number")
        }

        return investorDAO.getInvestorCommitments(investorInt)
    }
}