package com.revolut.transfer.dao;

import com.revolut.transfer.vo.Account;
import io.dropwizard.hibernate.AbstractDAO;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

public class AccountDAO extends AbstractDAO<Account> {

    public AccountDAO(SessionFactory factory) {
        super(factory);
    }

    /**
     * Find account by its number
     * @param accountNumber
     * @return
     */
    public Optional<Account> findByNumber(Long accountNumber) {
        return Optional.ofNullable(get(accountNumber));
    }

    /**
     * Create or update an account
     * @param account
     * @return
     */
    public Account save(Account account) {
        return persist(account);
    }

    /**
     * Get all accounts in the DB
     * @return
     */
    public List<Account> findAll() {
        return list(query("from Account"));
    }

}
