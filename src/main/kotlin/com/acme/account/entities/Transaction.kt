package com.acme.account.models

import java.math.BigDecimal
import java.util.Date
import java.time.Instant
import javax.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate

class TransactionSourceOrDestinationNullException(message: String): Exception(message)
class TransactionSameSourceAndDestinationException(message: String): Exception(message)
class TransactionAmountZeroException(message: String): Exception(message)

@Entity
data class Transaction (
  // FIXME: need to have createdAt and updatedAt 
  @Id @GeneratedValue val id: Long = 0,

// can also have sourceType and destinationType if we need to handle Withdrawal and Deposit
  val sourceId: Long,
  val amount: BigDecimal,
  val destinationId: Long,

  var startedAt: Instant,
  var succeededAt: Instant? = null,
  var failedAt: Instant? = null,
  var failMessage: String? = null,
) {
  fun succeed() {
    this.succeededAt = Instant.now()
  }

  fun fail(e: Exception) {
    this.failedAt = Instant.now()
    this.failMessage = e.message
  }

  fun validate(): Boolean {
    if (sourceId == 0L || destinationId == 0L) {
      throw TransactionSourceOrDestinationNullException("Source or Destination cannot be null")
    }

    if (sourceId == destinationId) {
      throw TransactionSameSourceAndDestinationException("Source and Detination cannot be the same")
    }

    if (amount == BigDecimal(0.0)) {
      throw TransactionAmountZeroException("amount cannot be zero")
    }

    return true
  }

  companion object {
    fun createForTransfer(sourceId: Long, destinationId: Long, amount: BigDecimal): Transaction {
      return Transaction(
        sourceId = sourceId,
        destinationId = destinationId,
        amount = amount,

        startedAt = Instant.now(), 
      )
    }
  }
}


