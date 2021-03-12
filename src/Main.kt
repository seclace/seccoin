import com.seccoin.blockchain.Block
import com.seccoin.blockchain.BlockChain
import com.seccoin.wallet.Transaction
import com.seccoin.wallet.TransactionOutput
import com.seccoin.wallet.Wallet

fun main() {
    val blockChain = BlockChain()
    val w1 = Wallet.create(blockChain)
    val w2 = Wallet.create(blockChain)

    fun logBalances() {
        println("wallet1 balance: ${w1.balance}")
        println("wallet2 balance: ${w2.balance}")
    }

    fun initWallet(wallet: Wallet, amount: Int): Transaction {
        val tx = Transaction(wallet.publicKey, wallet.publicKey, amount)
        tx.outputs.add(TransactionOutput(wallet.publicKey, amount, tx.hash))
        return tx.sign(wallet.privateKey)
    }

    fun transfer(from: Wallet, to: Wallet, amount: Int, prevHash: String, blockData: String): Block {
        val b = Block(prevHash, blockData)
        val tx = from.sendFundsTo(to.publicKey, amount)
        b.addTransaction(tx)
        val mined = blockChain.add(b)

        logBalances()

        return mined
    }

    logBalances()

    val genesisBlock = Block("0", "I'm the first block!")

    val tx1 = initWallet(w1, amount = 100)
    val tx2 = initWallet(w2, amount = 100)

    genesisBlock.addTransaction(tx1)
    genesisBlock.addTransaction(tx2)
    val minedBlock1 = blockChain.add(genesisBlock)

    logBalances()

    val minedBlock2 = transfer(w1, w2, 10, minedBlock1.hash, "I'm the second block!")
    val minedBlock3 = transfer(w2, w1, 25, minedBlock2.hash, "I'm the third block!")
    val minedBlock4 = transfer(w1, w2, 115, minedBlock3.hash, "I'm the fourth block!")

    println("Is blockchain valid? ${blockChain.isValid()}")
}
