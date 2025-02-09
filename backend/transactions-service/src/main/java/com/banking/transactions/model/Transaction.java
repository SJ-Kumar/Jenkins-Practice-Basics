package com.banking.transactions.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String username;  
    private String accountNumber;
    private String type;  
    private double amount;
    private String recipientAccount;
    private LocalDateTime timestamp;

    // ✅ Explicit constructor excluding 'id'
    public Transaction(String username, String accountNumber, String type, double amount, String recipientAccount, LocalDateTime timestamp) {
        this.setUsername(username);
        this.setAccountNumber(accountNumber);
        this.setType(type);
        this.setAmount(amount);
        this.setRecipientAccount(recipientAccount);
        this.setTimestamp(timestamp);
    }

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRecipientAccount() {
		return recipientAccount;
	}

	public void setRecipientAccount(String recipientAccount) {
		this.recipientAccount = recipientAccount;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
}
