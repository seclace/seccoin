package com.seccoin.wallet

import com.seccoin.common.encodeToString
import com.seccoin.common.hash
import java.security.PublicKey

data class TransactionOutput(
    val recipient: PublicKey,
    val amount: Int,
    val txHash: String,
    var hash: String = "",
) {
    init {
        hash = "${recipient.encodeToString()}$txHash$amount".hash()
    }

    fun isMine(me: PublicKey): Boolean {
        return recipient == me
    }
}
