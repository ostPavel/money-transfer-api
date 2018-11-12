package com.revolut.transfer.dao;

import com.revolut.transfer.enums.Currency;
import com.revolut.transfer.vo.Transaction;
import io.dropwizard.testing.junit.DAOTestRule;
import javax.validation.ConstraintViolationException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.Assert.assertNotNull;

public class TransactionDAOTest {

    @Rule
    public DAOTestRule daoTestRule = DAOTestRule.newBuilder()
            .addEntityClass(Transaction.class)
            .build();

    private TransactionDAO transactionDAO;

    private List<Transaction> txs = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        transactionDAO = new TransactionDAO(daoTestRule.getSessionFactory());

        for (int i = 0; i < 3; i++) {
            Transaction txToSave = new Transaction();
            txToSave.setFromAccNumber(i);
            txToSave.setToAccNumber(i + 1);
            txToSave.setAmount(new BigDecimal(1000));
            txToSave.setCurrency(Currency.GBP);
            txToSave.setSuccess(true);
            txs.add(txToSave);
        }
    }

    @Test
    public void testSaveTransaction() {
        int index = 0;
        final Transaction tx = daoTestRule.inTransaction(
                () -> transactionDAO.save(txs.get(index))
        );
        assertNotNull(tx.getId());
        assertThat(tx.getFromAccNumber()).isEqualTo(index);
        assertThat(tx.getToAccNumber()).isEqualTo(index + 1);
        assertThat(tx.getAmount()).isEqualTo(new BigDecimal(1000));
        assertThat(tx.getCurrency()).isEqualTo(Currency.GBP);
    }

    @Test
    public void testfindAllTransactions() {
        daoTestRule.inTransaction(() -> {
            transactionDAO.save(txs.get(0));
            transactionDAO.save(txs.get(1));
            transactionDAO.save(txs.get(2));
        });

        final List<Transaction> txs = transactionDAO.findAll();
        assertThat(txs).hasSize(3);
    }

    @Test
    public void handleNullAmount() {
        // checks the @NotNull constraint
        assertThatExceptionOfType(ConstraintViolationException.class).isThrownBy(()->
                daoTestRule.inTransaction(() -> transactionDAO.save(new Transaction())));
    }
}
