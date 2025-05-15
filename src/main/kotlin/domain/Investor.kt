package com.jdulfer.domain

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class Investor(
    val id: Int,
    val name: String,
    val investoryType: String,
    val country: String,
    val dateAdded: LocalDate,
    val lastUpdated: LocalDate,
    val commitmentTotal: Long
)
