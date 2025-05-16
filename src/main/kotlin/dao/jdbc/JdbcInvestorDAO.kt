package com.jdulfer.dao.jdbc

import com.jdulfer.dao.InvestorDAO
import com.jdulfer.dao.jdbc.tables.CommitmentTable
import com.jdulfer.dao.jdbc.tables.InvestorTable
import com.jdulfer.domain.Commitment
import com.jdulfer.domain.Investor
import io.ktor.server.plugins.*
import kotlinx.datetime.LocalDate
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.sum
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class JdbcInvestorDAO : InvestorDAO {
    override suspend fun getInvestors(): List<Investor> {
        val investors = newSuspendedTransaction {
            SchemaUtils.create(InvestorTable)
            SchemaUtils.create(CommitmentTable)

            InvestorTable.join(
                otherTable = CommitmentTable,
                joinType = JoinType.INNER,
                onColumn = InvestorTable.id,
                otherColumn = CommitmentTable.investorId
            ).select(
                InvestorTable.id,
                InvestorTable.name,
                InvestorTable.investoryType,
                InvestorTable.country,
                InvestorTable.dateAdded,
                InvestorTable.lastUpdated,
                CommitmentTable.amount.sum()
            ).groupBy(InvestorTable.id).map {
                Investor(
                    id = it[InvestorTable.id],
                    name = it[InvestorTable.name],
                    investoryType = it[InvestorTable.investoryType],
                    country = it[InvestorTable.country],
                    dateAdded = LocalDate.parse(it[InvestorTable.dateAdded]),
                    lastUpdated = LocalDate.parse(it[InvestorTable.lastUpdated]),
                    commitmentTotal = it[CommitmentTable.amount.sum()]
                        ?: throw NotFoundException("Could not find commitment total for investor ${it[InvestorTable.name]}")
                )
            }
        }

        return investors
    }

    override suspend fun getInvestorCommitments(investorId: Int): List<Commitment> {
        val commitments = newSuspendedTransaction {
            SchemaUtils.create(CommitmentTable)

            CommitmentTable.selectAll().where { CommitmentTable.investorId eq investorId }.map {
                Commitment(
                    id = it[CommitmentTable.id],
                    amount = it[CommitmentTable.amount],
                    currency = it[CommitmentTable.currency],
                    assetClass = it[CommitmentTable.assetClass]
                )
            }
        }

        return commitments
    }
}