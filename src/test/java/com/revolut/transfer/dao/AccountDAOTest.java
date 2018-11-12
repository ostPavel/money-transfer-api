package com.revolut.transfer.dao;

import com.revolut.transfer.enums.Currency;
import com.revolut.transfer.vo.Account;
import io.dropwizard.testing.junit.DAOTestRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

public class AccountDAOTest {

    @Rule
    public DAOTestRule daoTestRule = DAOTestRule.newBuilder()
            .addEntityClass(Account.class)
            .build();

    private AccountDAO accountDAO;

    private List<Account> accounts = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        accountDAO = new AccountDAO(daoTestRule.getSessionFactory());

        for (int i = 0; i < 3; i++) {
            Account account = new Account();
            account.setBalance(new BigDecimal(1000).multiply(new BigDecimal(i)));
            account.setCurrency(Currency.getByCode(i));
            accounts.add(account);
        }
    }

    @Test
    public void testFindByNumberSuccessful() {
        final Account acc = daoTestRule.inTransaction(
                () -> accountDAO.save(accounts.get(0))
        );

        assertNotNull(accountDAO.findByNumber(acc.getAccountNumber()));
    }

    @Test
    public void testFindByNumberUnsuccessful() {
        final Account acc = daoTestRule.inTransaction(
                () -> accountDAO.save(accounts.get(0))
        );

        assertThat(accountDAO.findByNumber(acc.getAccountNumber() + 999)).isEqualTo(Optional.empty());
    }

    @Test
    public void testSave() {
        final Account acc = daoTestRule.inTransaction(
                () -> accountDAO.save(accounts.get(0))
        );
        assertThat(acc.getAccountNumber()).isGreaterThan(0);
        assertThat(acc.getCurrency().getCode()).isEqualTo(0);
    }

    @Test
    public void testFindAll() {
        daoTestRule.inTransaction(() -> {
            accountDAO.save(accounts.get(0));
            accountDAO.save(accounts.get(1));
            accountDAO.save(accounts.get(2));
        });

        final List<Account> accs = accountDAO.findAll();
        assertThat(accs).hasSize(3);
    }
}