package guru.qa.niffler.data.tpl;

import com.atomikos.icatch.jta.UserTransactionImp;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

/**
 * Для распределенных транзакций
 **/
public class XaTransactionTemplate {
    private final JdbcConnectionHolders holders;
    private AtomicBoolean closeAfterAction = new AtomicBoolean(true);

    public XaTransactionTemplate(String... jdbcUrls) {
        this.holders = Connections.getHolders(jdbcUrls);
    }

    @SafeVarargs
    public final <T> T execute(Supplier<T>... suppliers) {
        UserTransaction userTransaction = new UserTransactionImp();
        try {
            userTransaction.begin();
            T result = null;

            for (Supplier<T> supplier : suppliers) {
                result = supplier.get();
            }
            userTransaction.commit();

            return result;
        } catch (Exception exception) {
            try {
                userTransaction.rollback();
            } catch (SystemException ex) {
                throw new RuntimeException(exception);
            }
            throw new RuntimeException(exception);
        } finally {
            if (closeAfterAction.get()) {
                holders.close();
            }
        }
    }

    public XaTransactionTemplate holdConnectionAfterAction(JdbcConnectionHolder holder) {
        this.closeAfterAction.set(false);
        return this;
    }
}
