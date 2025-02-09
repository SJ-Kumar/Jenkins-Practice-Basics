package com.banking.accounts.service;

import com.banking.accounts.model.Account;
import com.banking.accounts.repository.AccountRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    // ✅ Create an account only for the logged-in user
    public Account createAccount(Account account) {
        String loggedInUsername = getCurrentUsername();
        account.setUsername(loggedInUsername);
        return accountRepository.save(account);
    }

    // ✅ Retrieve only logged-in user's accounts
    public List<Account> getUserAccounts() {
        String loggedInUsername = getCurrentUsername();
        return accountRepository.findByUsername(loggedInUsername);
    }

    // ✅ Retrieve a specific account only if it belongs to the logged-in user
    public Optional<Account> getAccountByNumber(String accountNumber) {
        String loggedInUsername = getCurrentUsername();
        Optional<Account> account = accountRepository.findByAccountNumber(accountNumber);

        // Check if the account belongs to the logged-in user
        return account.filter(acc -> acc.getUsername().equals(loggedInUsername));
    }

    // ✅ Update an account only if it belongs to the logged-in user
    public Optional<Account> updateAccount(Long id, Account updatedAccount) {
        String loggedInUsername = getCurrentUsername();
        Optional<Account> existingAccount = accountRepository.findById(id);

        if (existingAccount.isPresent() && existingAccount.get().getUsername().equals(loggedInUsername)) {
            updatedAccount.setId(id);
            updatedAccount.setUsername(loggedInUsername); // Ensure the username is not changed
            return Optional.of(accountRepository.save(updatedAccount));
        } else {
            return Optional.empty(); // Unauthorized update
        }
    }

    // ✅ Delete an account only if it belongs to the logged-in user
    public boolean deleteAccount(Long id) {
        String loggedInUsername = getCurrentUsername();
        Optional<Account> existingAccount = accountRepository.findById(id);

        if (existingAccount.isPresent() && existingAccount.get().getUsername().equals(loggedInUsername)) {
            accountRepository.deleteById(id);
            return true;
        } else {
            return false; // Unauthorized delete
        }
    }

    // ✅ Helper method to get the logged-in username
    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName(); // Retrieves the logged-in username
    }
}
