package com.seccoin.wallet

import com.seccoin.blockchain.BlockChain
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey

data class Wallet(val privateKey: PrivateKey, val publicKey: PublicKey, val blockChain: BlockChain) {
    companion object {
        fun create(blockChain: BlockChain): Wallet {
            val generator = KeyPairGenerator.getInstance("RSA")
            generator.initialize(2048)
            val keyPair = generator.genKeyPair()

            return Wallet(keyPair.private, keyPair.public, blockChain)
        }
    }

    val balance: Int get() {
        return getMyTransactions().sumBy { it.amount }
    }

    fun sendFundsTo(recipient: PublicKey, amount: Int): Transaction {
        if (balance < amount) {
            throw Exception("Insufficient funds")
        }

        val tx = Transaction(publicKey, recipient, amount)
        tx.outputs.add(TransactionOutput(recipient, amount, tx.hash))

        var collectedAmount = 0
        for (myTx in getMyTransactions()) {
            collectedAmount += myTx.amount
            tx.inputs.add(myTx)

            if (collectedAmount > amount) {
                val change = collectedAmount - amount
                tx.outputs.add(TransactionOutput(publicKey, change, tx.hash))
            }

            if (collectedAmount >= amount) {
                break
            }
        }

        return tx.sign(privateKey)
    }

    private fun getMyTransactions(): Collection<TransactionOutput> {
        return blockChain.UTXO.filterValues { it.isMine(publicKey) }.values
    }
}
