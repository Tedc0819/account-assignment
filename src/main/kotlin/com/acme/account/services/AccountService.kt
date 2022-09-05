package com.acme.account.services

import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired 
import com.acme.account.models.Account
import com.acme.account.models.Transaction
import com.acme.account.repositories.AccountRepository
import java.math.BigDecimal
import java.util.UUID
import com.acme.account.dtos.*

@Service
class AccountService {
	@Autowired
	private lateinit var accountRepo: AccountRepository 

  fun getAccount(accountNumber: Long): Account {
    val account = this.accountRepo.getAccount(accountNumber) 

    return account 
  }

  fun transfer(sourceId: Long, destinationId: Long, amount: BigDecimal): Transaction {
    var transaction: Transaction? = null

    try {
      // REMARK: init a record first to make sure every transaction request is logged
      transaction = this.accountRepo.initTransferTransaction(sourceId, destinationId, amount)

      transaction.validate()

      transaction = this.accountRepo.completeTransferTransaction(transaction.id)
    } catch (e: Exception) {
      if (transaction != null) {
        transaction = this.accountRepo.failTransaction(transaction!!.id, e)
      }

      throw e
    }

    return transaction
  }
}