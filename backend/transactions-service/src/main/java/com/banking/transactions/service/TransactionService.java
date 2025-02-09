package com.banking.transactions.service;

import com.banking.transactions.model.Transaction;
import com.banking.transactions.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountServiceClient accountServiceClient;

    public Transaction deposit(String username, String accountNumber, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive.");
        }

        double updatedBalance = accountServiceClient.getBalance(username) + amount;
        if (!accountServiceClient.updateBalance(username, updatedBalance)) {
            throw new RuntimeException("Failed to update balance.");
        }

        Transaction transaction = new Transaction(username, accountNumber, "DEPOSIT", amount, null, LocalDateTime.now());
        return transactionRepository.save(transaction);
    }

    public Transaction withdraw(String username, String accountNumber, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive.");
        }

        double currentBalance = accountServiceClient.getBalance(username);
        if (currentBalance < amount) {
            throw new RuntimeException("Insufficient balance.");
        }

        double updatedBalance = currentBalance - amount;
        if (!accountServiceClient.updateBalance(username, updatedBalance)) {
            throw new RuntimeException("Failed to update balance.");
        }

        Transaction transaction = new Transaction(username, accountNumber, "WITHDRAW", amount, null, LocalDateTime.now());
        return transactionRepository.save(transaction);
    }

    public Transaction transfer(String username, String senderAccount, String recipientAccount, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive.");
        }

        double senderBalance = accountServiceClient.getBalance(username);
        if (senderBalance < amount) {
            throw new RuntimeException("Insufficient balance.");
        }

        double senderUpdatedBalance = senderBalance - amount;
        double recipientUpdatedBalance = accountServiceClient.getBalance(recipientAccount) + amount;

        if (!accountServiceClient.updateBalance(username, senderUpdatedBalance) ||
            !accountServiceClient.updateBalance(username, recipientUpdatedBalance)) {
            throw new RuntimeException("Transfer failed.");
        }

        Transaction transaction = new Transaction(username, senderAccount, "TRANSFER", amount, recipientAccount, LocalDateTime.now());
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getUserTransactions(String username) {
        return transactionRepository.findByUsername(username);
    }
}
