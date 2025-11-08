package guru.qa.niffler.data;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.jdbc.AtomikosDataSourceBean;
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

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Databases {

    private final static Map<String, DataSource> dataSources = new ConcurrentHashMap<>();
    private final static Map<Long, Map<String, Connection>> threadConnections = new ConcurrentHashMap<>();

    public record XaFunction<T>(Function<Connection, T> function, String jdbcUrl, int isolationLevel) {
    }

    public record XaConsumer(Consumer<Connection> consumer, String jdbcUrl, int isolationLevel) {
    }

    @SafeVarargs
    public static <T> T transaction(XaFunction<T>... functions) {
        UserTransaction userTransaction = new UserTransactionImp();
        try {
            userTransaction.begin();
            T result = null;
            for (XaFunction<T> function : functions) {
                result = function.function.apply(getConnection(function.jdbcUrl, function.isolationLevel));
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

    public static <T> void transaction(XaConsumer... consumers) {
        UserTransaction userTransaction = new UserTransactionImp();
        try {
            userTransaction.begin();
            for (XaConsumer consumer : consumers) {
                consumer.consumer.accept(getConnection(consumer.jdbcUrl(), consumer.isolationLevel));
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

    public static Connection getConnection(String jdbcUrl, int isolation) {
        return threadConnections.computeIfAbsent(
                Thread.currentThread().threadId(),
                key -> {
                    try {
                        var connect = getDataSource(jdbcUrl).getConnection();
                        connect.setTransactionIsolation(isolation);
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

    private static DataSource getDataSource(String jdbcUrl) {
        return dataSources.computeIfAbsent(jdbcUrl,
                key -> {
                    AtomikosDataSourceBean datasourceBean = new AtomikosDataSourceBean();
                    final String dbName = StringUtils.substringAfter(jdbcUrl, "5432/");
                    datasourceBean.setUniqueResourceName(dbName);
                    datasourceBean.setXaDataSourceClassName("org.postgresql.xa.PGXADataSource");

                    Properties properties = new Properties();
                    properties.setProperty("URL", jdbcUrl);
                    properties.setProperty("user", "postgres");
                    properties.setProperty("user", "secret");
                    datasourceBean.setXaProperties(properties);
                    datasourceBean.setPoolSize(10);

                    return datasourceBean;
                });
    }
}
