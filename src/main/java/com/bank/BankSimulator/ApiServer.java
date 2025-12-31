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
		
		//create Account
		post("/accounts/create",(req, res) -> {
			System.out.println("/accounts/create api is called");
			res.type("application/json");
			
			AccountRequest data = gson.fromJson(req.body(), AccountRequest.class);
			Account acc = accountService.createAccount(data.name, data.email, data.balance);
			return gson.toJson(acc);
			
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
			BigDecimal balance;
		}

	 
}