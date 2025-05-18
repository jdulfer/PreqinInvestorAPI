package com.jdulfer.unit

import com.jdulfer.dao.jdbc.JdbcInvestorDAO
import com.jdulfer.dao.jdbc.tables.CommitmentTable
import com.jdulfer.dao.jdbc.tables.InvestorTable
import io.ktor.server.plugins.*
import kotlinx.coroutines.test.runTest
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class JdbcInvestorDAOTest {
    private lateinit var dao: JdbcInvestorDAO

    @Before
    fun setup() {
        Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")

        transaction {
            SchemaUtils.drop(InvestorTable, CommitmentTable)
            SchemaUtils.create(InvestorTable, CommitmentTable)
        }

        dao = JdbcInvestorDAO()
    }

    @Test
    fun `test getInvestors returns correct data`() = runTest {
        transaction {
            InvestorTable.insert {
                it[id] = 1
                it[name] = "Cassian Andor"
                it[investoryType] = "Rebel Rogue"
                it[country] = "GC"
                it[dateAdded] = "2024-01-01"
                it[lastUpdated] = "2024-01-01"
            }

            CommitmentTable.insert {
                it[id] = 1
                it[investorId] = 1
                it[amount] = 1000000
                it[currency] = "GC"
                it[assetClass] = "Rebel Activity"
            }

            CommitmentTable.insert {
                it[id] = 2
                it[investorId] = 1
                it[amount] = 500000
                it[currency] = "GC"
                it[assetClass] = "Intelligence"
            }
        }

        val investors = dao.getInvestors()

        assertEquals(1, investors.size)
        assertEquals("Cassian Andor", investors[0].name)
        assertEquals(1500000, investors[0].commitmentTotal)
        assertEquals("Rebel Rogue", investors[0].investoryType)
        assertEquals("GC", investors[0].country)
    }

    @Test
    fun `test getInvestorCommitments returns correct commitments`() = runTest {
        transaction {
            InvestorTable.insert {
                it[id] = 1
                it[name] = "Cassian Andor"
                it[investoryType] = "Rebel Rogue"
                it[country] = "GC"
                it[dateAdded] = "2024-01-01"
                it[lastUpdated] = "2024-01-01"
            }

            CommitmentTable.insert {
                it[id] = 1
                it[investorId] = 1
                it[amount] = 1000000
                it[currency] = "GC"
                it[assetClass] = "Rebel Activity"
            }

            CommitmentTable.insert {
                it[id] = 2
                it[investorId] = 1
                it[amount] = 500000
                it[currency] = "GC"
                it[assetClass] = "Intelligence"
            }
        }

        val commitments = dao.getInvestorCommitments(1)

        assertEquals(2, commitments.size)
        assertEquals(1000000, commitments[0].amount)
        assertEquals("GC", commitments[0].currency)
        assertEquals("Rebel Activity", commitments[0].assetClass)
        assertEquals(500000, commitments[1].amount)
        assertEquals("Intelligence", commitments[1].assetClass)
    }

    @Test
    fun `test getInvestorCommitments throws NotFoundException for non-existent investor`() = runTest {
        assertFailsWith<NotFoundException> {
            dao.getInvestorCommitments(999)
        }
    }
}