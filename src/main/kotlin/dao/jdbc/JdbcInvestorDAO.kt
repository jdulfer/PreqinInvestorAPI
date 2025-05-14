package com.jdulfer.dao.jdbc

import com.jdulfer.dao.InvestorDAO
import com.jdulfer.dao.jdbc.tables.CommitmentTable
import com.jdulfer.dao.jdbc.tables.InvestorTable
import com.jdulfer.domain.Commitment
import com.jdulfer.domain.Investor
import kotlinx.datetime.LocalDate
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class JdbcInvestorDAO : InvestorDAO {
    override suspend fun getInvestors(): List<Investor> {
        val investors = newSuspendedTransaction {
            SchemaUtils.create(InvestorTable)

            InvestorTable.selectAll().map {
                Investor(
                    id = it[InvestorTable.id],
                    name = it[InvestorTable.name],
                    investoryType = it[InvestorTable.investoryType],
                    country = it[InvestorTable.country],
                    dateAdded = LocalDate.parse(it[InvestorTable.dateAdded]),
                    lastUpdated = LocalDate.parse(it[InvestorTable.lastUpdated])
                )
            }
        }

        return investors
    }

    override suspend fun getInvestorCommitments(investorId: Int): List<Commitment> {
        val commitments = newSuspendedTransaction {
            SchemaUtils.create(CommitmentTable)

            CommitmentTable.selectAll().where { CommitmentTable.investorId eq investorId}.map {
                Commitment(
                    id = it[CommitmentTable.id],
                    amount = it[CommitmentTable.amount].toLong(),
                    currency = it[CommitmentTable.currency],
                    assetClass = it[CommitmentTable.assetClass]
                )
            }
        }

        return commitments
    }
}