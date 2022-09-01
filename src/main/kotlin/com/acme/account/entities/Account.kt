package com.acme.account.models

import java.math.BigDecimal
import java.util.Date
import java.time.Instant
import javax.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate

@Entity
data class Account (
  @Id val accountNumber: String, 
  var balance: BigDecimal = BigDecimal(0.0),
/* 
  @CreatedDate
  private val createdAt: Instant,

  @LastModifiedDate
  private val updatedAt: Instant
*/
)