package guru.qa.niffler.data;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import guru.qa.niffler.model.enums.TrnIsolationLevel;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

import static guru.qa.niffler.model.enums.TrnIsolationLevel.READ_COMMITED;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Databases {

    private final static Map<String, DataSource> dataSources = new ConcurrentHashMap<>();
    private final static Map<Long, Map<String, Connection>> threadConnections = new ConcurrentHashMap<>();

    public record XaFunction<T>(Function<Connection, T> function, String jdbcUrl) {
    }

    public record XaConsumer(Consumer<Connection> consumer, String jdbcUrl) {
    }

    public static <T> T transaction(Function<Connection, T> function, String jdbcUrl) {
        return transaction(function, jdbcUrl, READ_COMMITED);
    }

    public static void transaction(Consumer<Connection> consumer, String jdbcUrl) {
        transaction(consumer, jdbcUrl, READ_COMMITED);
    }

    @SafeVarargs
    public static <T> T xaTransaction(XaFunction<T>... functions) {
        return xaTransaction(READ_COMMITED, functions);
    }

    public static void xaTransaction(XaConsumer... consumers) {
        xaTransaction(READ_COMMITED, consumers);
    }

    public static <T> T transaction(Function<Connection, T> function, String jdbcUrl, TrnIsolationLevel isolationLvl) {
        Connection connection = null;
        try {
            connection = getConnection(jdbcUrl, isolationLvl);
            connection.setAutoCommit(false);
            T result = function.apply(connection);
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
        }
    }

    public static void transaction(Consumer<Connection> consumer, String jdbcUrl, TrnIsolationLevel isolationLvl) {
        Connection connection = null;
        try {
            connection = getConnection(jdbcUrl, isolationLvl);
            connection.setAutoCommit(false);
            consumer.accept(connection);
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            try {
                connection.rollback();
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }

    @SafeVarargs
    public static <T> T xaTransaction(TrnIsolationLevel isolationLvl, XaFunction<T>... functions) {
        UserTransaction userTransaction = new UserTransactionImp();
        try {
            userTransaction.begin();
            T result = null;

            for (XaFunction<T> function : functions) {
                var connection = getConnection(function.jdbcUrl, isolationLvl);
                result = function.function.apply(connection);
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
        }
    }

    public static void xaTransaction(TrnIsolationLevel isolationLvl, XaConsumer... consumers) {
        UserTransaction userTransaction = new UserTransactionImp();
        try {
            userTransaction.begin();
            for (XaConsumer consumer : consumers) {
                var connection = getConnection(consumer.jdbcUrl, isolationLvl);
                consumer.consumer.accept(connection);
            }
            userTransaction.commit();
        } catch (Exception exception) {
            try {
                userTransaction.rollback();
            } catch (SystemException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(exception);
        }
    }


    public static void closeAll() {
        threadConnections.values().stream()
                .flatMap(v -> v.values().stream())
                .forEach(connection -> {
                    try {
                        if (connection != null && !connection.isClosed()) {
                            connection.close();
                        }
                    } catch (SQLException ignored) {
                    }
                });
    }

    public static Connection getConnection(String jdbcUrl, TrnIsolationLevel isolation) {
        return threadConnections.computeIfAbsent(
                Thread.currentThread().threadId(),
                key -> {
                    try {
                        var connect = getDataSource(jdbcUrl).getConnection();
                        connect.setTransactionIsolation(isolation.getLevel());
                        return new HashMap<>(Map.of(
                                jdbcUrl,
                                connect
                        ));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
        ).computeIfAbsent(
                jdbcUrl,
                key -> {
                    try {
                        return getDataSource(jdbcUrl).getConnection();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }

    public static DataSource getDataSource(String jdbcUrl) {
        return dataSources.computeIfAbsent(jdbcUrl,
                key -> {
                    AtomikosDataSourceBean datasourceBean = new AtomikosDataSourceBean();
                    final String dbName = StringUtils.substringAfter(jdbcUrl, "5432/");
                    datasourceBean.setUniqueResourceName(dbName);
                    datasourceBean.setXaDataSourceClassName("org.postgresql.xa.PGXADataSource");

                    Properties properties = new Properties();
                    properties.setProperty("URL", jdbcUrl);
                    properties.setProperty("user", "postgres");
                    properties.setProperty("password", "secret");
                    datasourceBean.setXaProperties(properties);
                    datasourceBean.setPoolSize(10);

                    return datasourceBean;
                });
    }
}
