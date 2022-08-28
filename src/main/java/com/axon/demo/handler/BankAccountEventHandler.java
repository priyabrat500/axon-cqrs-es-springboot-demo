package com.axon.demo.handler;

import com.axon.demo.entity.BankAccount;
import com.axon.demo.event.AccountCreatedEvent;
import com.axon.demo.event.MoneyCreditedEvent;
import com.axon.demo.event.MoneyDebitedEvent;
import com.axon.demo.query.FindBankAccountQuery;
import com.axon.demo.repository.BankAccountRepository;
import com.axon.demo.exception.AccountNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class BankAccountEventHandler {

    private final BankAccountRepository repository;
    private final QueryUpdateEmitter updateEmitter;


    @EventHandler
    public void on(AccountCreatedEvent event) {
        log.info("Handling a Bank Account creation command {}", event.getId());
        BankAccount bankAccount = new BankAccount(
                event.getId(),
                event.getOwner(),
                event.getInitialBalance()
        );
        this.repository.save(bankAccount);
    }

    @EventHandler
    public void on(MoneyCreditedEvent event) throws AccountNotFoundException {
        log.info("Handling a Bank Account Credit command {}", event.getId());
        Optional<BankAccount> optionalBankAccount = this.repository.findById(event.getId());
        if (optionalBankAccount.isPresent()) {
            BankAccount bankAccount = optionalBankAccount.get();
            bankAccount.setBalance(bankAccount.getBalance().add(event.getCreditAmount()));
            this.repository.save(bankAccount);
        } else {
            throw new AccountNotFoundException(event.getId());
        }
    }

    @EventHandler
    public void on(MoneyDebitedEvent event) throws AccountNotFoundException {
        log.info("Handling a Bank Account Debit command {}", event.getId());
        Optional<BankAccount> optionalBankAccount = this.repository.findById(event.getId());
        if (optionalBankAccount.isPresent()) {
            BankAccount bankAccount = optionalBankAccount.get();
            bankAccount.setBalance(bankAccount.getBalance().subtract(event.getDebitAmount()));
            this.repository.save(bankAccount);
        } else {
            throw new AccountNotFoundException(event.getId());
        }
    }

    @QueryHandler
    public BankAccount handle(FindBankAccountQuery query) {
        log.info("Handling FindBankAccountQuery query: {}", query);
        return this.repository.findById(query.getAccountId()).orElse(null);
    }
}
