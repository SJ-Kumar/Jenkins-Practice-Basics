package com.banking.transactions.controller;

import com.banking.transactions.model.Transaction;
import com.banking.transactions.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/deposit")
    public Transaction deposit(@RequestParam String username, @RequestParam String accountNumber, @RequestParam double amount) {
        return transactionService.deposit(username, accountNumber, amount);
    }

    @PostMapping("/withdraw")
    public Transaction withdraw(@RequestParam String username, @RequestParam String accountNumber, @RequestParam double amount) {
        return transactionService.withdraw(username, accountNumber, amount);
    }

    @PostMapping("/transfer")
    public Transaction transfer(@RequestParam String username, @RequestParam String senderAccount, @RequestParam String recipientAccount, @RequestParam double amount) {
        return transactionService.transfer(username, senderAccount, recipientAccount, amount);
    }

    @GetMapping("/{username}")
    public List<Transaction> getUserTransactions(@PathVariable String username) {
        return transactionService.getUserTransactions(username);
    }
}
