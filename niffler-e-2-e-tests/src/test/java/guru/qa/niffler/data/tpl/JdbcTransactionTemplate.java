package guru.qa.niffler.data.tpl;

import guru.qa.niffler.model.enums.TxIsolationLevel;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import static guru.qa.niffler.model.enums.TxIsolationLevel.READ_COMMITED;

/**
 * Для обычного jdbc без Spring
 **/
public class JdbcTransactionTemplate {
    private final JdbcConnectionHolder holder;
    private final AtomicBoolean closeAfterAction = new AtomicBoolean(true);

    public JdbcTransactionTemplate(String jdbcUrl) {
        this.holder = Connections.getHolder(jdbcUrl);
    }

    public <T> T execute(Supplier<T> supplier, TxIsolationLevel isolationLvl) {
        Connection connection = null;
        try {
            connection = holder.getConnection();
            connection.setTransactionIsolation(isolationLvl.getLevel());
            connection.setAutoCommit(false);

            T result = supplier.get();
            connection.commit();
            connection.setAutoCommit(true);

            return result;
        } catch (SQLException e) {
            try {
                connection.rollback();
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        } finally {
            if (closeAfterAction.get()) {
                holder.close();
            }
        }
    }

    public <T> T execute(Supplier<T> supplier) {
        return execute(supplier, READ_COMMITED);
    }

    public JdbcTransactionTemplate holdConnectionAfterAction(JdbcConnectionHolder holder) {
        this.closeAfterAction.set(false);
        return this;
    }
}
