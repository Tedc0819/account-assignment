package com.acme.account.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.server.ResponseStatusException
import org.springframework.http.HttpStatus

import org.springframework.beans.factory.annotation.Autowired 
import com.acme.account.services.AccountService
import com.acme.account.models.Account
import com.acme.account.dtos.*
import com.acme.account.exceptions.*
import java.util.UUID

@RestController
@RequestMapping("/accounts")
class AccountResource() {
	@Autowired
	private lateinit var accountService: AccountService

	@GetMapping("/{accountNumber}")
	fun index(@RequestParam accountNumber:String): GetAccountResponseDto {
    try {
      val account = this.accountService.getAccount(accountNumber)

      return GetAccountResponseDto(account.accountNumber, account.balance) 
    } catch (e: Exception) {
      if (e is NotFoundException) {
        throw ResponseStatusException(HttpStatus.NOT_FOUND)
      }

      throw e
    }
	}
}