package com.jdulfer.dao.jdbc.tables

import org.jetbrains.exposed.sql.Table

const val MAX_STRING_LENGTH = 128

object InvestorTable : Table("investor") {
    const val MAX_STRING_LENGTH = 128

    val id = integer("id").autoIncrement()
    val name = varchar("name", MAX_STRING_LENGTH)
    val investoryType = varchar("investory_type", MAX_STRING_LENGTH)
    val country = varchar("country", MAX_STRING_LENGTH)
    val dateAdded = varchar("date_added", MAX_STRING_LENGTH)
    val lastUpdated = varchar("last_updated", MAX_STRING_LENGTH)

    override val primaryKey = PrimaryKey(id)
}