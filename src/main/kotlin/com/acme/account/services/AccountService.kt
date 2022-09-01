package com.acme.account.services

import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired 
import com.acme.account.models.Account
import com.acme.account.repositories.AccountRepository
import java.math.BigDecimal
import java.util.UUID
import com.acme.account.dtos.*
import com.acme.account.exceptions.*

@Service
class AccountService {
	@Autowired
	private lateinit var accountRepo: AccountRepository 

  fun getAccount(accountNumber: String): Account {
    val account = this.accountRepo.findOneAccount(accountNumber) 

    if (account == null) {
      throw NotFoundException()
    } 

    return account 
  }
}