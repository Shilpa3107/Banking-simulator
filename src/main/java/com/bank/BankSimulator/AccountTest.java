package com.bank.BankSimulator;

import java.math.BigDecimal;
 
import java.util.Collection;

import com.bank.BankSimulato.repository.AccountRepository;
import com.bank.BankSimulator.exceptions.AccountNotFoundException;
import com.bank.BankSimulator.exceptions.InvalidAmountException;
import com.bank.BankSimulator.model.Account;
import com.bank.BankSimulator.service.AccountService;

public class AccountTest {
	
	public static void main(String[] args) {
		 
		AccountRepository repo = new AccountRepository();
		AccountService service = new AccountService(repo);
		
		try {
			Account acc1 = service.createAccount("Krish", "krish@gmail.com", "pass", new BigDecimal("2000"));
			Account acc2 = service.createAccount("Radha", "radha@gmail.com", "pass", new BigDecimal("5000"));
			
			Account acc3 = service.createAccount("Krish", "krish@gmail.com", "pass", new BigDecimal("2000"));
			Account acc4 = service.createAccount("Radha", "radha@gmail.com", "pass", new BigDecimal("5000"));
			
			System.out.println("Created Accounts..");
			System.out.println(acc1);
			System.out.println(acc2);
			
			System.out.println("------------------------------");
			//get the Account based account number
			Account newAccount1 = service.getAccount("1000000");
			
			
			System.out.println("Getting the account based on accountNumber");
			System.out.println(newAccount1);
			
			
			System.out.println("->->->->->->->->->->->->->->->->");
			System.out.println("Collecting all accounts..");
			
			Collection<Account> allAccounts = service.listAll();
			for(Account a : allAccounts) {
				System.out.println(a);
			}
			
			
		} catch (InvalidAmountException | AccountNotFoundException e) {
			e.printStackTrace();
		}
	
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
	
		} catch (ClassNotFoundException e) {
			 			e.printStackTrace();
		}
		 
		
	}
}
