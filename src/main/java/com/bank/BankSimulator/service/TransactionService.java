package com.bank.BankSimulator.service;

import java.math.BigDecimal;

import com.bank.BankSimulato.repository.TransactionRepository;
import com.bank.BankSimulator.exceptions.AccountNotFoundException;
import com.bank.BankSimulator.exceptions.InsufficientBalanceException;
import com.bank.BankSimulator.exceptions.InvalidAmountException;
import com.bank.BankSimulator.model.Account;
import com.bank.BankSimulator.util.FileReportUtil;

public class TransactionService {
	private AccountService accountService;
	private TransactionRepository transactionRepository;
	private AlertService alertService;
	
	
	public TransactionService(AccountService accountService ,TransactionRepository transactionRepository,AlertService alertService) {
		this.accountService = accountService;
		this.transactionRepository = transactionRepository;
		this.alertService = alertService;
	}
	
	public void deposite(String accNo ,BigDecimal amount) throws InvalidAmountException, AccountNotFoundException {
		if(amount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new InvalidAmountException("Amount should not be negative");
		}
		
		Account acc = accountService.getAccount(accNo);
		acc.credit(amount);
		accountService.updateAccount(acc);
		
		FileReportUtil.writeLine("DEPOSITE | Acc: "+accNo+" | Amount: "+amount);
		
		transactionRepository.logTransaction("DEPOSITE", accNo, amount.doubleValue(), null);
		
		alertService.checkBalance(acc);
	 
	}
	
	
	public void withdraw(String accNo, BigDecimal amount) throws InvalidAmountException, AccountNotFoundException, InsufficientBalanceException {
		if(amount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new InvalidAmountException("Amount should not be negative");
		}
		
		Account account = accountService.getAccount(accNo);
		
		if(account.getBalance().compareTo(amount) < 0) {
			throw new InsufficientBalanceException("Insufficient Balance");
		}
		
		account.debit(amount);
		accountService.updateAccount(account);
		
		FileReportUtil.writeLine("WITHDRAW | Acc: "+accNo+" | Amount: "+amount);
		
		transactionRepository.logTransaction("WITHDRAW", accNo, amount.doubleValue(), null);
		
		alertService.checkBalance(account);
	}
	
	public void tranfer(String fromAcc,String toAcc,BigDecimal amount) throws AccountNotFoundException, InsufficientBalanceException {
		
		if(amount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("Amount should not be negative");
		}
		
		Account sender = accountService.getAccount(fromAcc);
		Account recevier = accountService.getAccount(toAcc);
		
		if(sender.getBalance().compareTo(amount) < 0) {
			throw new InsufficientBalanceException("Insufficient Balance");
		}
		
		sender.debit(amount);
		recevier.credit(amount);
		accountService.updateAccount(sender);
		accountService.updateAccount(recevier);
		
		FileReportUtil.writeLine("TRANSFER | FromAcc: "+fromAcc+" | ToAccount: "+toAcc+" | Amount "+amount);
		
		transactionRepository.logTransaction("TRANSFER", fromAcc, amount.doubleValue(), toAcc);
		
		alertService.checkBalance(sender);
		alertService.checkBalance(recevier);
	}
 
	 
}


