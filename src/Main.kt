import com.seccoin.blockchain.Block

fun main() {
    val block1 = Block("0", "I'm the first block!")
    val block2 = Block(block1.hash, "I'm the second block!")
    val block3 = Block(block2.hash, "I'm the third block!")

    println(block1)
    println(block2)
    println(block3)
}
