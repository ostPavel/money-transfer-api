package com.revolut.transfer;

import com.revolut.transfer.dao.AccountDAO;
import com.revolut.transfer.dao.TransactionDAO;
import com.revolut.transfer.resource.AccountResource;
import com.revolut.transfer.resource.TransactionResource;
import com.revolut.transfer.service.AccountService;
import com.revolut.transfer.service.TransactionService;
import com.revolut.transfer.vo.Account;
import com.revolut.transfer.vo.Transaction;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.db.ManagedDataSource;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;

/**
 * Dropwizard application class
 *
 */
@Slf4j
public class TransferApplication extends Application<TransferConfiguration> {

    public static void main(String[] args) throws Exception {
        new TransferApplication().run(args);
    }

    // dropwizard-hibernate module
    private final HibernateBundle<TransferConfiguration> hibernate = new HibernateBundle<TransferConfiguration>(Account.class, Transaction.class) {
        @Override
        public DataSourceFactory getDataSourceFactory(TransferConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };

    // dropwizard-migrations module
    private final MigrationsBundle<TransferConfiguration> migrations = new MigrationsBundle<TransferConfiguration>() {
        @Override
        public DataSourceFactory getDataSourceFactory(TransferConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };

    /**
     * Adding modules to the application
     * @param bootstrap
     */
    @Override
    public void initialize(Bootstrap<TransferConfiguration> bootstrap) {
        bootstrap.addBundle(hibernate);
        bootstrap.addBundle(migrations);
    }

    @Override
    public void run(TransferConfiguration config,
                    Environment environment) throws Exception {

        // liquibase script migration
        ManagedDataSource ds = config.getDataSourceFactory().build(environment.metrics(), "migrations");
        try (Connection connection = ds.getConnection()) {
            Liquibase migrator = new Liquibase("migrations.xml", new ClassLoaderResourceAccessor(), new JdbcConnection(connection));
            migrator.update("");
        } catch (Exception ex) {
            log.error("Failed to migrate DB: ", ex);
        }
        log.info("Finished Liquibase migration");

        final AccountDAO accountDAO = new AccountDAO(hibernate.getSessionFactory());
        final TransactionDAO transactionDao = new TransactionDAO(hibernate.getSessionFactory());

        // Registering JERSEY Singleton components
        AccountService accountService = new AccountService(accountDAO);
        environment.jersey().register(accountService);
        environment.jersey().register(new AccountResource(accountService));

        TransactionService transactionService = new TransactionService(transactionDao, accountDAO);
        environment.jersey().register(transactionService);
        environment.jersey().register(new TransactionResource(transactionService));
    }

    @Override
    public String getName() {
        return "money-transfer-api";
    }


}