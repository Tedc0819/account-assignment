package com.acme.account.models

import java.math.BigDecimal
import java.util.Date
import java.time.Instant
import javax.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate

class AccountAmountLessThanZeroException: Exception() 

@Entity
data class Account (
  // FIXME: need to have createdAt and updatedAt 
  @Id @GeneratedValue val accountNumber: Long = 0, 
  var balance: BigDecimal,
) {
  fun transfer(amount: BigDecimal): Account {
    if (this.balance - amount < BigDecimal(0.0)) {
      throw AccountAmountLessThanZeroException() 
    }

    this.balance = this.balance - amount

    return this
  }

  fun receiveTransfer(amount: BigDecimal): Account {
    this.balance = this.balance + amount

    return this
  }
}

