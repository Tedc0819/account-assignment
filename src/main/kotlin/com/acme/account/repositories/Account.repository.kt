package com.acme.account.repositories

import com.acme.account.models.Account
import org.springframework.beans.factory.annotation.Autowired 
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID
import com.acme.account.dtos.*

@Repository
interface AccountEntityRepository : JpaRepository<Account, String> {
  fun getAccountByAccountNumber(accountNumber: String): Account? 
}

@Repository
class AccountRepository {
	@Autowired
	private lateinit var accountEntityRepo: AccountEntityRepository 

  fun findOneAccount(accountNumber: String): Account? {
    val account = this.accountEntityRepo.findById(accountNumber).orElse(null)

    return account 
  } 
}

