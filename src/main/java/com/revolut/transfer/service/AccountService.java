package com.revolut.transfer.service;

import com.revolut.transfer.dao.AccountDAO;
import com.revolut.transfer.vo.Account;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;
import java.util.List;

@AllArgsConstructor
@Singleton
public class AccountService {

    private final AccountDAO dao;

    /**
     * Save an account
     * @param account
     * @return
     */
    public Account createAccount(Account account) {
        return dao.save(account);
    }

    /**
     * retrieve all accounts from DB
     * @return
     */
    public List<Account> getAllAccounts() {
        return dao.findAll();
    }
}
