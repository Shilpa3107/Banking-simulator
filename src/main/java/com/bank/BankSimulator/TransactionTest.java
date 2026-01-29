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

public class TransactionTest {
	
	public static void main(String[] args) {
		
		AccountRepository accRepo = new AccountRepository();
		
		TransactionRepository trxRepo = new TransactionRepository();
		AccountService accService = new AccountService(accRepo);
		AlertService alertService = new AlertService(new BigDecimal("1000"));
		TransactionService trx = new TransactionService(accService,trxRepo,alertService);
		
		/*try {
			Account acc = accService.createAccount("Hari", "hari@gmail.com", new BigDecimal("1000"));
			System.out.println(acc);
			
			System.out.println("----------------------------------------------");
			
			trx.deposite("1000000", new BigDecimal("2000"));
			System.out.println(acc);
			
			
		} catch (InvalidAmountException | AccountNotFoundException e) {
			 
			e.printStackTrace();
		}
		//trx.deposite(null, null);*/
		
		/*try {
			Account acc = accService.createAccount("Hari", "hari@gmail.com", new BigDecimal("10000"));
			System.out.println(acc);
			
			System.out.println("----------------------------------------------");
			
			trx.withdraw("1000000", new BigDecimal("2000"));
			System.out.println(acc);
			
			
		} catch (InvalidAmountException | AccountNotFoundException | InsufficientBalanceException e) {
			 
			e.printStackTrace();
		}*/
		
		
		try {
			Account fromAccount = accService.createAccount("Hari", "hari@gmail.com", "pass", new BigDecimal("10000"));
			Account toAccount = accService.createAccount("sita", "sita@gmail.com", "pass", new BigDecimal("20000"));
			System.out.println(fromAccount);
			System.out.println(toAccount);
			
			System.out.println("----------------------------------------------");
			
			trx.tranfer(fromAccount.getAccountNumber(), toAccount.getAccountNumber(), new BigDecimal("500"));
			
			System.out.println(fromAccount);
			System.out.println(toAccount);
			
		} catch (InvalidAmountException | AccountNotFoundException | InsufficientBalanceException e) {
			 
			e.printStackTrace();
		}
		
		
		
		 
		
	}

}
