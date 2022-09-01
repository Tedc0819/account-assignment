package com.acme.account.dtos

import java.math.BigDecimal
import java.util.UUID

data class GetAccountResponseDto(
  val accountNumber: String,
  val balance: BigDecimal,
)