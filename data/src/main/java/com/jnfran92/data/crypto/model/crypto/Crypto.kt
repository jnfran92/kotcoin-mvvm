package com.jnfran92.data.crypto.model.crypto

data class Crypto(
    val id: Long,
    val name: String,
    val symbol: String,
    val currentPriceLocal: Double
)