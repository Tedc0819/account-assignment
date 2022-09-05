package com.acme.account.models

import java.math.BigDecimal
import java.util.Date
import java.time.Instant
import javax.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import com.acme.account.models.Account
import com.acme.account.models.Transaction

@Entity
data class AccountTransaction (
  // FIXME: need to have createdAt and updatedAt 
  @Id @GeneratedValue val id: Long = 0, 

  val accountId: Long,
  val transactionId: Long,
  val amountChange: BigDecimal,
) {
  companion object {
    fun createForTransferTransaction(account: Account, transaction: Transaction): AccountTransaction {
      return AccountTransaction(
        accountId = account.accountNumber, 
        transactionId = transaction.id,
        amountChange = if (transaction.sourceId == account.accountNumber) -transaction.amount else transaction.amount
      )
    }
  }
}
