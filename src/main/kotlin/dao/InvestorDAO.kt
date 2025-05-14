package com.jdulfer.dao

import com.jdulfer.domain.Commitment
import com.jdulfer.domain.Investor

interface InvestorDAO {
    suspend fun getInvestors(): List<Investor>
    suspend fun getInvestorCommitments(investorId: Int): List<Commitment>
}