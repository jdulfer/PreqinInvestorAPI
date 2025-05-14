package com.jdulfer.dao.jdbc.tables

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object CommitmentTable : Table("commitment") {
    const val MAX_STRING_LENGTH = 128

    val id = integer("id").autoIncrement()
    val investorId = reference("investor_id", InvestorTable.id, onDelete = ReferenceOption.CASCADE)
    val amount = varchar("amount", MAX_STRING_LENGTH)
    val currency = varchar("currency", MAX_STRING_LENGTH)
    val assetClass = varchar("asset_class", MAX_STRING_LENGTH)

    override val primaryKey = PrimaryKey(id)
}