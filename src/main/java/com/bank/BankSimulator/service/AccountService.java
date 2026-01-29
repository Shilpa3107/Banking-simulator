package com.bank.BankSimulator.service;

import java.math.BigDecimal;
import java.util.Collection;

import com.bank.BankSimulato.repository.AccountRepository;
import com.bank.BankSimulator.exceptions.AccountNotFoundException;
import com.bank.BankSimulator.exceptions.InvalidAmountException;
import com.bank.BankSimulator.model.Account;

public class AccountService {
	
	private AccountRepository repo;
	
	public AccountService(AccountRepository repo) {
		this.repo = repo;
	}
	
	public Account createAccount(String holderName ,String email, String password, BigDecimal openingBalance) throws InvalidAmountException {
		if(openingBalance.compareTo(BigDecimal.ZERO) < 0) {
			throw new InvalidAmountException("Opening balance cannot be negative");
		}
		
		Account account = new Account(holderName,email,password,openingBalance);
		repo.save(account);
		return account;
	}

	public Account authenticate(String identifier, String password) throws AccountNotFoundException {
		Account account = repo.findAccountByIdentifier(identifier);
		if (account == null || !account.getPassword().equals(password)) {
			throw new AccountNotFoundException("Invalid credentials");
		}
		return account;
	}
	
	public Account getAccount(String accountNumber) throws AccountNotFoundException{
		Account account = repo.findAccountByNumber(accountNumber);
	
		if(account == null) {
			throw new AccountNotFoundException("Account not fount: "+accountNumber);
		}
		
		return account;
	}
	
	public Collection<Account> listAll(){
		return repo.findAll();
	}

	public void deleteAccount(String accountNumber) throws AccountNotFoundException {
		Account account = repo.findAccountByNumber(accountNumber);
		if (account == null) {
			throw new AccountNotFoundException("Account not found: " + accountNumber);
		}
		repo.delete(accountNumber);
	}

	public void updateAccount(Account account) {
		repo.save(account);
	}
	
}
