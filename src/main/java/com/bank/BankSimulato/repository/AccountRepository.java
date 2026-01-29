package com.bank.BankSimulato.repository;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.bank.BankSimulator.model.Account;

public class AccountRepository {

    public AccountRepository() {
        createTableIfNotExists();
        initializeCounter();
    }

    private void initializeCounter() {
        String query = "SELECT MAX(CAST(accountNumber AS UNSIGNED)) as max_acc FROM accounts";
        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                long maxAcc = rs.getLong("max_acc");
                if (maxAcc >= 1000000) {
                    Account.setCounter(maxAcc + 1);
                }
            }
        } catch (Exception e) {
            System.out.println("Counter Init Error: " + e.getMessage());
        }
    }

    private void createTableIfNotExists() {
        String query = "CREATE TABLE IF NOT EXISTS accounts (" +
                "accountNumber VARCHAR(20) PRIMARY KEY, " +
                "holderName VARCHAR(100), " +
                "email VARCHAR(100), " +
                "password VARCHAR(100), " +
                "balance DECIMAL(15,2))";
        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement()) {
            stmt.execute(query);
            System.out.println("Accounts table checked/created.");
        } catch (Exception e) {
            System.out.println("DB Table Error: " + e.getMessage());
        }
    }

    public void save(Account account) {
        String query = "INSERT INTO accounts (accountNumber, holderName, email, password, balance) " +
                "VALUES (?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE holderName=?, email=?, password=?, balance=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, account.getAccountNumber());
            pstmt.setString(2, account.getHolderName());
            pstmt.setString(3, account.getEmail());
            pstmt.setString(4, account.getPassword());
            pstmt.setBigDecimal(5, account.getBalance());
            // Update parts
            pstmt.setString(6, account.getHolderName());
            pstmt.setString(7, account.getEmail());
            pstmt.setString(8, account.getPassword());
            pstmt.setBigDecimal(9, account.getBalance());

            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("DB Save Error: " + e.getMessage());
        }
    }

    public Account findAccountByNumber(String accountNumber) {
        String query = "SELECT * FROM accounts WHERE accountNumber = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, accountNumber);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAccount(rs);
                }
            }
        } catch (Exception e) {
            System.out.println("DB Find Error: " + e.getMessage());
        }
        return null;
    }

    public Account findAccountByIdentifier(String identifier) {
        String query = "SELECT * FROM accounts WHERE accountNumber = ? OR email = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, identifier);
            pstmt.setString(2, identifier);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAccount(rs);
                }
            }
        } catch (Exception e) {
            System.out.println("DB Identifier Error: " + e.getMessage());
        }
        return null;
    }

    public Collection<Account> findAll() {
        List<Account> accounts = new ArrayList<>();
        String query = "SELECT * FROM accounts";
        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                accounts.add(mapResultSetToAccount(rs));
            }
        } catch (Exception e) {
            System.out.println("DB FindAll Error: " + e.getMessage());
        }
        return accounts;
    }

    public void delete(String accountNumber) {
        String query = "DELETE FROM accounts WHERE accountNumber = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, accountNumber);
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("DB Delete Error: " + e.getMessage());
        }
    }

    private Account mapResultSetToAccount(ResultSet rs) throws SQLException {
        return new Account(
                rs.getString("accountNumber"),
                rs.getString("holderName"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getBigDecimal("balance")
        );
    }
}
