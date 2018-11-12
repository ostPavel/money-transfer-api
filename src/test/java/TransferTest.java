import com.revolut.transfer.TransferApplication;
import com.revolut.transfer.TransferConfiguration;
import com.revolut.transfer.enums.Currency;
import com.revolut.transfer.vo.Account;
import com.revolut.transfer.vo.Transaction;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * A proper end-to-end test with real HTTP requests
 */
public class TransferTest {

    @ClassRule
    public static final DropwizardAppRule<TransferConfiguration> RULE =
            new DropwizardAppRule<TransferConfiguration>(TransferApplication.class, ResourceHelpers.resourceFilePath("test-config.yml"));

    @Test
    public void fullTest() {

        // add an account
        final Account account = new Account();
        account.setCurrency(Currency.RUB);
        account.setBalance(new BigDecimal(10000));
        final Account newAccount = RULE.client().target("http://localhost:" + RULE.getLocalPort() + "/account/create")
                .request()
                .post(Entity.entity(account, MediaType.APPLICATION_JSON_TYPE))
                .readEntity(Account.class);

        // 5 accounts have been inserted with a Liquibase script on boot
        assertEquals(6, newAccount.getAccountNumber());


        String transactionToItselfJson = "{\"fromAccNumber\":\"3\",\"toAccNumber\":\"3\",\"amount\":\"10\"}";
        postTransfer(transactionToItselfJson);

        String transactionInsufficientFundsJson = "{\"fromAccNumber\":\"2\",\"toAccNumber\":\"3\",\"amount\":\"10\"}";
        postTransfer(transactionInsufficientFundsJson);

        String validTransactionJson = "{\"fromAccNumber\":\"1\",\"toAccNumber\":\"3\",\"amount\":\"100\"}";
        postTransfer(validTransactionJson);

        ArrayList<Transaction> transactions = RULE.client().target("http://localhost:" + RULE.getLocalPort() + "/transaction/list")
                .request()
                .get(Response.class).readEntity(new GenericType<ArrayList<Transaction>>() {
                });

        assertFalse(transactions.get(0).isSuccess());
        assertFalse(transactions.get(1).isSuccess());
        assertTrue(transactions.get(2).isSuccess());

        transactions.forEach(System.out::println);
    }

    private void postTransfer(String jsonString) {
        RULE.client().target("http://localhost:" + RULE.getLocalPort() + "/transaction/transfer")
                .request()
                .post(Entity.json(jsonString));
    }
}