package com.jdulfer.domain

import kotlinx.serialization.Serializable

@Serializable
data class Commitment(
    val id: Int,
    val amount: Long,
    val currency: String,
    val assetClass: String
)