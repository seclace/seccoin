import com.seccoin.blockchain.Block
import com.seccoin.blockchain.BlockChain

fun main() {
    val genesisBlock = Block("0", "I'm the first block!")

    val blockChain = BlockChain()
    val minedBlock1 = blockChain.add(genesisBlock)
    val block2 = Block(minedBlock1.hash, "I'm the second block!")
    val minedBlock2 = blockChain.add(block2)
    val block3 = Block(minedBlock2.hash, "I'm the third block!")
    blockChain.add(block3)

    println("Is blockchain valid? ${blockChain.isValid()}")
}
