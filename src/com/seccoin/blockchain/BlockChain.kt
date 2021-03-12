package com.seccoin.blockchain

import com.seccoin.miner.isMined
import com.seccoin.miner.mine

class BlockChain {
    private val chain: MutableList<Block> = mutableListOf()

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
}
