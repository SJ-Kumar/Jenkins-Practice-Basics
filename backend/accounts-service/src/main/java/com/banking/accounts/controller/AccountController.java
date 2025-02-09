package com.banking.accounts.controller;

import com.banking.accounts.model.Account;
import com.banking.accounts.repository.AccountRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountRepository accountRepository;

    public AccountController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    // ✅ Retrieve only logged-in user's accounts
    @GetMapping
    public List<Account> getUserAccounts() {
        String loggedInUsername = getCurrentUsername();
        return accountRepository.findByUsername(loggedInUsername);
    }

    // ✅ Create an account for the logged-in user
    @PostMapping
    public String createAccount(@RequestBody Account account) {
        String loggedInUsername = getCurrentUsername();
        account.setUsername(loggedInUsername);
        accountRepository.save(account);
        return "Account created successfully!";
    }

    // ✅ Update only the logged-in user's account
    @PutMapping("/{id}")
    public String updateAccount(@PathVariable Long id, @RequestBody Account account) {
        String loggedInUsername = getCurrentUsername();

        Optional<Account> existingAccount = accountRepository.findById(id);
        if (existingAccount.isPresent() && existingAccount.get().getUsername().equals(loggedInUsername)) {
            account.setId(id);
            account.setUsername(loggedInUsername);
            accountRepository.save(account);
            return "Account updated successfully!";
        } else {
            return "Unauthorized to update this account!";
        }
    }
    @GetMapping("/balance")
    public Double getAccountBalance(@RequestParam String username) {
        return accountRepository.findByUsername(username)
                .stream()
                .findFirst()
                .map(Account::getBalance)
                .orElseThrow(() -> new RuntimeException("Account not found for user: " + username));
    }
    
    @PutMapping("/updateBalance")
    public String updateBalance(@RequestParam String username, @RequestParam double newBalance) {
        Optional<Account> accountOpt = accountRepository.findByUsername(username).stream().findFirst();

        if (accountOpt.isPresent()) {
            Account account = accountOpt.get();
            account.setBalance(newBalance);
            accountRepository.save(account);
            return "Balance updated successfully for user: " + username;
        } else {
            return "Account not found for user: " + username;
        }
    }

    // ✅ Delete only the logged-in user's account
    @DeleteMapping("/{id}")
    public String deleteAccount(@PathVariable Long id) {
        String loggedInUsername = getCurrentUsername();

        Optional<Account> existingAccount = accountRepository.findById(id);
        if (existingAccount.isPresent() && existingAccount.get().getUsername().equals(loggedInUsername)) {
            accountRepository.deleteById(id);
            return "Account deleted successfully!";
        } else {
            return "Unauthorized to delete this account!";
        }
    }

    // ✅ Helper method to get the logged-in username
    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName(); // This gets the username from Basic Authentication
    }
}
