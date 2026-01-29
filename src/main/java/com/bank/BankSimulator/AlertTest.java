package com.bank.BankSimulator;

import java.math.BigDecimal;

import com.bank.BankSimulato.repository.AccountRepository;
import com.bank.BankSimulato.repository.TransactionRepository;
import com.bank.BankSimulator.exceptions.AccountNotFoundException;
import com.bank.BankSimulator.exceptions.InsufficientBalanceException;
import com.bank.BankSimulator.exceptions.InvalidAmountException;
import com.bank.BankSimulator.model.Account;
import com.bank.BankSimulator.service.AccountService;
import com.bank.BankSimulator.service.AlertService;
import com.bank.BankSimulator.service.TransactionService;

public class AlertTest {
	public static void main(String[] args) {
		
		AccountRepository accountRepository = new AccountRepository();
		AccountService accountService = new AccountService(accountRepository);
		
		TransactionRepository trxRepo = new TransactionRepository();
		AlertService alertService = new AlertService(new BigDecimal("1000"));
		
		TransactionService trxService = new TransactionService(accountService,trxRepo,alertService);
		
		/*try {
			Account account = accountService.createAccount("chinni", "chinnikrishnamekala1@gmail.com", new BigDecimal("500"));
			trxService.deposite(account.getAccountNumber(),new BigDecimal("2000"));
			System.out.println("Amount is deposited Successfully..");
			System.out.println("Total Balance : "+account.getBalance());
		} catch (InvalidAmountException | AccountNotFoundException e) {
			 
			e.printStackTrace();
		}*/
		
		try {
			Account account = accountService.createAccount("Chinnikrishna", "chinnikrishnamekala1@gmail.com", "pass", new BigDecimal("5000"));
			trxService.withdraw(account.getAccountNumber(),new BigDecimal("4500"));
			System.out.println("Amount is withdrawn Successfully..");
			System.out.println("Total Balance : "+account.getBalance());
			
		} catch (InvalidAmountException | AccountNotFoundException | InsufficientBalanceException e) {
			 
			e.printStackTrace();
		}
		
		
		
		
	}
}
