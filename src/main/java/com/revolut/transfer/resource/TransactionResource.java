package com.revolut.transfer.resource;

import com.revolut.transfer.service.TransactionService;
import com.revolut.transfer.vo.Transaction;
import com.revolut.transfer.vo.TransactionResponse;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.FlushMode;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("transaction")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
@AllArgsConstructor
public class TransactionResource {

    private final TransactionService transactionService;

    @POST
    @Path("transfer")
    @UnitOfWork(flushMode = FlushMode.ALWAYS)
    public TransactionResponse transferMoney(Transaction transaction) {
        log.info("==> transferMoney({})", transaction);
        TransactionResponse result = transactionService.transfer(transaction);
        log.info("<== transferMoney({})", result);
        return result;
    }

    @GET
    @Path("/list")
    @UnitOfWork
    public List<Transaction> getTransactions() {
        log.info("==> getTransactions()");
        List<Transaction> result = transactionService.getAllTransactions();
        log.info("<== getTransactions()");
        return result;
    }

}
