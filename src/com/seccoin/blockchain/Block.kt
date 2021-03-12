package com.seccoin.blockchain

import com.seccoin.common.hash
import java.time.Instant

data class Block(
    val previousHash: String,
    val data: String,
    val timestamp: Long = Instant.now().toEpochMilli(),
    val nonce: Long = 0,
    var hash: String = "",
) {

    init {
        hash = calculateHash()
    }

    fun calculateHash(): String {
        return "$previousHash$timestamp$data$nonce".hash()
    }
}
