package com.revolut.transfer.resource;

import com.revolut.transfer.enums.Currency;
import com.revolut.transfer.service.AccountService;
import com.revolut.transfer.vo.Account;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.validation.Validated;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.FlushMode;
import org.hibernate.validator.constraints.NotEmpty;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.List;


/**
 * Aux controller to create accounts.
 */
@Path("account")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
@AllArgsConstructor
public class AccountResource {

    private final AccountService accountService;

    @POST
    @Path("/create")
    @UnitOfWork(flushMode = FlushMode.ALWAYS)
    public Account createAccount(@NotNull @Valid Account account) {
        log.info("==> createAccount({})", account);
        Account result = accountService.createAccount(account);
        log.info("<== createAccount({})", result);
        return result;
    }

    @GET
    @Path("/list")
    @UnitOfWork
    public List<Account> viewAccounts() {
        log.info("==> viewAccounts()");
        List<Account> result = accountService.getAllAccounts();
        log.info("<== viewAccounts");
        return result;
    }


}
