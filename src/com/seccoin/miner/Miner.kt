package com.seccoin.miner

import com.seccoin.blockchain.Block

private const val difficulty = 5
private val validPrefix = "0".repeat(difficulty)

fun isMined(block: Block): Boolean {
    return block.hash.startsWith(validPrefix)
}

fun mine(block: Block): Block {
    println("Mining: $block")

    var minedBlock = block.copy()
    while (!isMined(minedBlock)) {
        minedBlock = minedBlock.copy(nonce = minedBlock.nonce + 1)
    }

    println("Mined: $minedBlock")

    return minedBlock
}
