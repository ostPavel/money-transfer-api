package com.revolut.transfer.dao;

import com.revolut.transfer.vo.Transaction;
import io.dropwizard.hibernate.AbstractDAO;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.UUID;

public class TransactionDAO extends AbstractDAO<Transaction> {
    public TransactionDAO(SessionFactory factory) {
        super(factory);
    }

    /**
     * Create a transaction
     * @param transaction
     * @return
     */
    public Transaction save(Transaction transaction) {
        return persist(transaction);
    }

    /**
     * Show all transactions
     * @return
     */
    public List<Transaction> findAll() {
        return list(query("from Transaction"));
    }

}
