package com.acme.account.dtos

import java.math.BigDecimal
import java.util.UUID

data class CreateTransactionRequestDto(
  val sourceId: Long,
  val amount: BigDecimal,
  val destinationId: Long,
)

data class CreateTransactionResponseDto(
  val transactionId: Long,
)