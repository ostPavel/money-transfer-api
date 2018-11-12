package com.revolut.transfer.service;

import com.revolut.transfer.dao.AccountDAO;
import com.revolut.transfer.dao.TransactionDAO;
import com.revolut.transfer.enums.Currency;
import com.revolut.transfer.vo.Account;
import com.revolut.transfer.vo.Transaction;
import com.revolut.transfer.vo.TransactionResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;
import javax.ws.rs.WebApplicationException;
import java.math.BigDecimal;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Singleton
public class TransactionService {

    private final TransactionDAO transactionDao;
    private final AccountDAO accountDAO;

    /**
     * Retrieve all transactions from DB
     * @return
     */
    public List<Transaction> getAllTransactions(){
        return transactionDao.findAll();
    }

    /**
     * This method transfers money from one account to another if the transaction is valid
     * Conversion is done if necessary.
     * @param tx
     * @return
     */
    public TransactionResponse transfer(Transaction tx) throws WebApplicationException {
        TransactionResponse result = new TransactionResponse(false, "init");
        try {
            if (tx == null) throw new WebApplicationException("Null transaction!"); // basic check

            if (tx.getFromAccNumber() == tx.getToAccNumber()) throw new WebApplicationException("Select a different account to transfer money to.");

            // after this point, we work with 2 different accounts:
            Account from = accountDAO.findByNumber(tx.getFromAccNumber()).orElseThrow(() -> new WebApplicationException("Account " + tx.getFromAccNumber() + " not found"));
            tx.setCurrency(from.getCurrency());
            Account to = accountDAO.findByNumber(tx.getToAccNumber()).orElseThrow(() -> new WebApplicationException("Account " + tx.getToAccNumber() + " not found"));
            if (from.getBalance().compareTo(tx.getAmount()) < 0) throw new WebApplicationException("Insufficient funds");

            // all checks are complete by this point
            transferWithConversion(from, to, tx.getAmount());

            result.setSuccess(true);
            result.setMessage("Transaction success");
        } catch (Exception ex) {
            result.setSuccess(false);
            result.setMessage("Could not transfer money: " + ex.getMessage());
            log.error("Transaction unsuccessful: " + ex.getMessage());
        } finally {
            tx.setSuccess(result.isSuccess());
            transactionDao.save(tx); // all transactions are saved
            log.info("Saved transaction {}", tx);
        }
        return result;
    }

    private void transferWithConversion(Account from, Account to, BigDecimal amount) {

        from.setBalance(from.getBalance().subtract(amount));
        BigDecimal amountTo = amount.multiply(getRate(from.getCurrency(), to.getCurrency()));
        to.setBalance(to.getBalance().add(amountTo));

        accountDAO.save(from);
        accountDAO.save(to);
    }

    // this could be just another table with conversion rates
    private BigDecimal getRate(Currency from, Currency to) {
        // TODO conversion service integration
        return new BigDecimal(1);
    }

}
