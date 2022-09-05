package com.acme.account.repositories

import com.acme.account.models.Account
import com.acme.account.models.Transaction
import com.acme.account.models.AccountTransaction
import org.springframework.beans.factory.annotation.Autowired 
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID
import com.acme.account.dtos.*
import com.acme.account.exceptions.*

@Repository
interface AccountEntityRepository : JpaRepository<Account, Long> {
  fun getAccountByAccountNumber(accountNumber: Long): Account? 
}

@Repository
interface TransactionEntityRepository : JpaRepository<Transaction, Long> {}

@Repository
interface AccountTransactionEntityRepository : JpaRepository<AccountTransaction, Long> {}

@Repository
class AccountRepository {
	@Autowired
	private lateinit var accountEntityRepo: AccountEntityRepository 
	@Autowired
	private lateinit var transactionEntityRepo: TransactionEntityRepository 
	@Autowired
	private lateinit var accountTransactionEntityRepo: AccountTransactionEntityRepository 

  fun getAccount(accountNumber: Long): Account {
    val account = this.accountEntityRepo.findById(accountNumber).orElse(null)

    if (account == null) {
      throw NotFoundException("Account NOT FOUND with ID: $accountNumber")
    } 

    return account 
  } 

  fun getTransaction(id: Long): Transaction {
    val transaction = this.transactionEntityRepo.findById(id).orElse(null)

    if (transaction == null) {
      throw NotFoundException("Transaction NOT FOUND with ID: $id")
    } 

    return transaction 
  } 

  fun initTransferTransaction(sourceId: Long, destinationId: Long, amount: BigDecimal): Transaction{
    val transaction = Transaction.createForTransfer(sourceId, destinationId, amount)

    val entity = this.transactionEntityRepo.saveAndFlush(transaction)

    return transaction
  }

  // should be a db transaction
  fun completeTransferTransaction(transactionId: Long): Transaction {
    var transaction = this.getTransaction(transactionId)

    val srcAccount = this.getAccount(transaction.sourceId)
    val destAccount = this.getAccount(transaction.destinationId)

    // as validation
    srcAccount.transfer(transaction.amount)
    destAccount.receiveTransfer(transaction.amount)

    val srcAccountTransaction = AccountTransaction.createForTransferTransaction(srcAccount, transaction)
    val destAccountTransaction = AccountTransaction.createForTransferTransaction(destAccount, transaction)

    this.accountEntityRepo.saveAndFlush(srcAccount)     
    this.accountEntityRepo.saveAndFlush(destAccount)     

    this.accountTransactionEntityRepo.saveAndFlush(srcAccountTransaction)
    this.accountTransactionEntityRepo.saveAndFlush(destAccountTransaction)

    transaction.succeed()

    transaction = this.transactionEntityRepo.saveAndFlush(transaction)

    return transaction
  }

  fun failTransaction(transactionId: Long, e: Exception): Transaction {
    var transaction = this.getTransaction(transactionId)

    transaction.fail(e)

    transaction = this.transactionEntityRepo.saveAndFlush(transaction)

    return transaction
  }
}

