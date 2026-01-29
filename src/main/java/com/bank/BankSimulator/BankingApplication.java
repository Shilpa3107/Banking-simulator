package com.bank.BankSimulator;

import java.math.BigDecimal;
import java.util.Scanner;

import com.bank.BankSimulato.repository.AccountRepository;
import com.bank.BankSimulato.repository.TransactionRepository;
import com.bank.BankSimulator.exceptions.AccountNotFoundException;
import com.bank.BankSimulator.exceptions.InsufficientBalanceException;
import com.bank.BankSimulator.exceptions.InvalidAmountException;
import com.bank.BankSimulator.model.Account;
import com.bank.BankSimulator.service.AccountService;
import com.bank.BankSimulator.service.AlertService;
import com.bank.BankSimulator.service.TransactionService;

public class BankingApplication {
	
	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		AccountRepository accRepo = new AccountRepository();
		AccountService accService = new AccountService(accRepo);
		TransactionRepository trxRepo = new TransactionRepository();
		AlertService alertService = new AlertService(new BigDecimal("1000"));
		
		TransactionService trxService = new TransactionService(accService, trxRepo, alertService);
		
		System.out.println("============================================================");
		System.out.println("         WELCOME TO BANK SIMULATOR APPLICATION        ");
		System.out.println("============================================================");
		
		
		boolean running = true;
		
		while(running) {
			System.out.println("\nChoose an option");
			System.out.println("1. Create Account");
			System.out.println("2. Deposite Money");
			System.out.println("3. Withdraw Money");
			System.out.println("4. Transfer Money");
			System.out.println("5. Show Account Details");
			System.out.println("6. List All Accounts");
			System.out.println("7. Exit");
			
			System.out.println("Enter Choice: ");
			int choice = sc.nextInt();
			sc.nextLine();
			
			switch(choice) {
				case 1:
					System.out.println("Enter name :");
					String name = sc.nextLine();
					System.out.println("Enter Email :");
					String email = sc.nextLine();
					System.out.println("Enter Password :");
					String password = sc.nextLine();
					System.out.println("Enter opening balance :");
					BigDecimal openingBalance = sc.nextBigDecimal();
				try {
					Account account = accService.createAccount(name, email, password, openingBalance);
					System.out.println("Account created successfully: "+" Account Number : "+account.getAccountNumber());
				} catch (InvalidAmountException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
				
				case 2:
					System.out.println("Enter account number: ");
					String accNumber = sc.nextLine();
					System.out.println("Enter amount to Deposite");
					BigDecimal amount = sc.nextBigDecimal();
				try {
					trxService.deposite(accNumber, amount);
					System.out.println("Deposite successfull");
					
					
				} catch (InvalidAmountException e) {
		
					e.printStackTrace();
				} catch (AccountNotFoundException e) {
					
					e.printStackTrace();
				}
				break;
				
				case 3:
					System.out.println("Enter account number: ");
					String accNumber1 = sc.nextLine();
					System.out.println("Enter amount to Withdraw");
					BigDecimal amount1 = sc.nextBigDecimal();
				try {
					trxService.withdraw(accNumber1, amount1);
					System.out.println("Withdraw successfull");
				} catch (InvalidAmountException | AccountNotFoundException | InsufficientBalanceException e) {
					 
					e.printStackTrace();
				}
				break;
				
				case 4:
					System.out.println("Enter Sender AccountNumber");
					String sAcc = sc.nextLine();
					System.out.println("Enter receiver AccountNumber");
					String rAcc = sc.nextLine();
					System.out.println("Enter amount to transfer");
					BigDecimal transferAmount = sc.nextBigDecimal();
					
				try {
					trxService.tranfer(sAcc, rAcc, transferAmount);
					System.out.println("Amount Transfer successfull");
				} catch (AccountNotFoundException | InsufficientBalanceException e) {
					
					e.printStackTrace();
				}
				
				break;
				
				case 5:
					System.out.println("Enter Account Number To get the Details");
					String showAccountNumber = sc.nextLine();
				try {
					Account account = accService.getAccount(showAccountNumber);
					System.out.println("Account Detatils : "+ account);
				} catch (AccountNotFoundException e) {
				
					e.printStackTrace();
				}
				
				break;
				
				case 6:
					System.out.println("Listing all accounts");
					
					for(Account acc : accService.listAll()) {
						System.out.println(acc);
					}
					break;
					
				case 7:
					System.out.println("Thank you for using our Banking Application");
					running = false;
					break;
					
				default:
					System.out.println("Invalid choice. Please enter valid choice");
				
			 
			}

		}
		
		 		
		
	}
}
