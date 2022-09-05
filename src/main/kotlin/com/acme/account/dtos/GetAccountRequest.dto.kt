package com.acme.account.dtos

import java.math.BigDecimal
import java.util.UUID

data class GetAccountResponseDto(
  val accountNumber: Long,
  val balance: BigDecimal,
)