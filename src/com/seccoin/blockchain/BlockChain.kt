package com.seccoin.blockchain

import com.seccoin.wallet.TransactionOutput

class BlockChain {
    private val chain: MutableList<Block> = mutableListOf()
    private val difficulty = 5
    private val validPrefix = "0".repeat(difficulty)
    val UTXO: MutableMap<String, TransactionOutput> = mutableMapOf()

    fun add(block: Block): Block {
        val minedBlock = if (isMined(block)) block else mine(block)
        chain.add(minedBlock)
        return minedBlock
    }

    fun isValid(): Boolean {
        when {
            chain.isEmpty() -> return true
            chain.size == 1 -> return chain[0].hash == chain[0].calculateHash()
            else -> {
                for (i in 1 until chain.size) {
                    val prevBlock = chain[i - 1]
                    val currentBlock = chain[i]

                    when {
                        currentBlock.hash != currentBlock.calculateHash() -> return false
                        currentBlock.previousHash != prevBlock.calculateHash() -> return false
                        !(isMined(prevBlock) && isMined(currentBlock)) -> return false
                    }
                }
                return true
            }
        }
    }

    private fun isMined(block: Block): Boolean {
        return block.hash.startsWith(validPrefix)
    }

    private fun mine(block: Block): Block {
        println("Mining: $block")

        var minedBlock = block.copy()
        while (!isMined(minedBlock)) {
            minedBlock = minedBlock.copy(nonce = minedBlock.nonce + 1)
        }

        println("Mined: $minedBlock")
        updateUTXO(minedBlock)

        return minedBlock
    }

    private fun updateUTXO(block: Block) {
        block.transactions.flatMap { it.inputs }.map { it.hash }.forEach { UTXO.remove(it) }
        block.transactions.flatMap { it.outputs }.forEach { UTXO[it.hash] = it }
    }
}
