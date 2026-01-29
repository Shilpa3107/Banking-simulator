package com.bank.BankSimulator;

import java.math.BigDecimal;

import static spark.Spark.*;

import com.bank.BankSimulato.repository.AccountRepository;
import com.bank.BankSimulato.repository.TransactionRepository;
import com.bank.BankSimulator.model.Account;
import com.bank.BankSimulator.service.AccountService;
import com.bank.BankSimulator.service.AlertService;
import com.bank.BankSimulator.service.TransactionService;
import com.google.gson.Gson;

import spark.Route;

public class ApiServer {
	
	public static void main(String[] args) {
		
		port(8080);
		enableCORS();
		
		Gson gson = new Gson();
		
		AccountRepository accRepo = new AccountRepository();
		
		AccountService accountService = new AccountService(accRepo);
		
		TransactionRepository trxRepo = new TransactionRepository();
		
		AlertService alertService = new AlertService(new BigDecimal("1000"));
		TransactionService trxService = new TransactionService(accountService,trxRepo,alertService);
		
		System.out.println("Spark server started on port number 8080");
		
		//create Account API
		post("/accounts/create",(req, res) -> {
			System.out.println("/accounts/create api is called");
			res.type("application/json");
			
			AccountRequest data = gson.fromJson(req.body(), AccountRequest.class);
			Account acc = accountService.createAccount(data.name, data.email, data.password, data.balance);
			return gson.toJson(acc);
			
		});

		// Login API
		post("/accounts/login", (req, res) -> {
			System.out.println("/accounts/login api is called");
			res.type("application/json");
			LoginRequest data = gson.fromJson(req.body(), LoginRequest.class);
			try {
				Account acc = accountService.authenticate(data.identifier, data.password);
				return gson.toJson(acc);
			} catch (Exception e) {
				res.status(401);
				return gson.toJson("Invalid credentials");
			}
		});

		// Admin Login API
		post("/admin/login", (req, res) -> {
			System.out.println("/admin/login api is called");
			res.type("application/json");
			LoginRequest data = gson.fromJson(req.body(), LoginRequest.class);
			if ("admin@bank.com".equals(data.identifier) && "admin123".equals(data.password)) {
				return gson.toJson("Admin Success");
			} else {
				res.status(401);
				return gson.toJson("Invalid Admin Credentials");
			}
		});
		
		
		//Deposite API
		post("/transactions/deposite",(req, res) ->{
			System.out.println("transactions/deposite api is called");
			try {
			  TxRequest data = gson.fromJson(req.body(), TxRequest.class);
			  trxService.deposite(data.accNo, data.amount);
			  return "Deposite successfully..!";
			} catch (Exception e) {
				e.printStackTrace();
				res.status(400);
				return "Error: " + e.getMessage();
			}
		});
		
		//Withdraw API
		post("/transactions/withdraw",(req, res) ->{
			System.out.println("/transactions/withdraw api is called");
			try {
				TxRequest data = gson.fromJson(req.body(), TxRequest.class);
				trxService.withdraw(data.accNo, data.amount);
				return "Withdraw successfully..!";
			} catch (Exception e) {
				e.printStackTrace();
				res.status(400);
				return "Error: " + e.getMessage();
			}
		});
		
		post("/transactions/transfer",(req, res) -> {
			System.out.println("/transactions/tranfer api is called");
			try {
				TransferRequest data = gson.fromJson(req.body(), TransferRequest.class);
				trxService.tranfer(data.fromAcc, data.toAcc, data.amount);
				return "Transfer successfully..!";
			} catch (Exception e) {
				e.printStackTrace();
				res.status(400);
				return "Error: " + e.getMessage();
			}
		});
		
		
		// IMPORTANT: /accounts/all must come BEFORE /accounts/:accNo to avoid route collision
		get("/accounts/all",(req,res) -> {
			System.out.println("/accounts/all api is called");
			res.type("application/json");
			return gson.toJson(accountService.listAll());
		});
		
		get("/accounts/:accNo",(req,res) ->{
			System.out.println("/accounts/acc api is called");
			res.type("application/json");
			String accNo = req.params("accNo");
			try {
				Account acc = accountService.getAccount(accNo);
				return gson.toJson(acc);
				
			}
			catch(Exception e) {
				res.status(404);
				return gson.toJson("Account not found");
			}
		});

		delete("/admin/accounts/:accNo", (req, res) -> {
			System.out.println("/admin/accounts/delete api is called");
			String accNo = req.params("accNo");
			try {
				accountService.deleteAccount(accNo);
				return "Account deleted successfully";
			} catch (Exception e) {
				res.status(404);
				return "Account not found";
			}
		});
		
		
		
		
	}
	
	public static void enableCORS(){
		options("/*",(request ,response) ->{
			String reqheaders = request.headers("Access-Control-Request-Headers");
			
			if(reqheaders != null) {
				response.header("Access-Control-Allow-Headers",reqheaders);
			}
			return "OK";
		});
		
		before((request,response) ->{
			response.header("Access-Control-Allow-Origin","*");
			response.header("Access-Control-Allow-Methods","GET,POST,DELETE,OPTIONS,PUT");
			response.header("Access-Control-Allow-Headers","Content-Type,Authorization");
			
		});
		
		 
		
	}
	
	
 
	
	static class AccountRequest{
			String name;
			String email;
			String password;
			BigDecimal balance;
		}

	static class LoginRequest {
		String identifier;
		String password;
	}
	
	static class TxRequest{
		String accNo;
		BigDecimal amount;
	}
	
	static class TransferRequest{
		String fromAcc;
		String toAcc;
		BigDecimal amount;
	}

	
	
	
	
	
	
	
	
	
	
	
	 
}