package com.axon.demo.service;

import com.axon.demo.command.DebitMoneyCommand;
import com.axon.demo.entity.BankAccount;
import com.axon.demo.controller.dto.AccountCreationDTO;
import com.axon.demo.command.CreateAccountCommand;
import com.axon.demo.command.CreditMoneyCommand;
import com.axon.demo.controller.dto.MoneyAmountDTO;
import lombok.AllArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class AccountCommandService {
    private final CommandGateway commandGateway;

    public CompletableFuture<BankAccount> createAccount(AccountCreationDTO creationDTO) {
        return this.commandGateway.send(new CreateAccountCommand(
                UUID.randomUUID(),
                creationDTO.getInitialBalance(),
                creationDTO.getOwner()
        ));
    }

    public CompletableFuture<String> creditMoneyToAccount(String accountId,
                                                          MoneyAmountDTO moneyCreditDTO) {
        return this.commandGateway.send(new CreditMoneyCommand(
                ServiceUtils.formatUuid(accountId),
                moneyCreditDTO.getAmount()
        ));
    }

    public CompletableFuture<String> debitMoneyFromAccount(String accountId,
                                                           MoneyAmountDTO moneyDebitDTO) {
        return this.commandGateway.send(new DebitMoneyCommand(
                ServiceUtils.formatUuid(accountId),
                moneyDebitDTO.getAmount()
        ));
    }
}
