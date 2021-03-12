package com.seccoin.wallet

import com.seccoin.common.encodeToString
import com.seccoin.common.hash
import com.seccoin.common.sign
import com.seccoin.common.verifySignature
import java.security.PrivateKey
import java.security.PublicKey

class Transaction(
    private val sender: PublicKey,
    private val recipient: PublicKey,
    private val amount: Int,
    var hash: String = "",
    val inputs: MutableList<TransactionOutput> = mutableListOf(),
    val outputs: MutableList<TransactionOutput> = mutableListOf(),
    private var signature: ByteArray = ByteArray(0),
) {

    companion object {
        var salt: Long = 0
            get() = ++field
    }

    init {
        hash = "${sender.encodeToString()}${recipient.encodeToString()}$amount$salt".hash()
    }

    fun sign(privateKey: PrivateKey): Transaction {
        signature = "${sender.encodeToString()}${recipient.encodeToString()}$amount".sign(privateKey)
        return this
    }

    fun isSignatureValid(): Boolean {
        return "${sender.encodeToString()}${recipient.encodeToString()}$amount".verifySignature(sender, signature)
    }
}
