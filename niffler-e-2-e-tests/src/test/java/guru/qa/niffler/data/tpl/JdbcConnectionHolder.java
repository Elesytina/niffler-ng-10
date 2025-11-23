package guru.qa.niffler.data.tpl;

import lombok.RequiredArgsConstructor;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class JdbcConnectionHolder implements AutoCloseable {

    private final DataSource dataSource;
    private final Map<Long, Connection> connections = new ConcurrentHashMap<>();

    public Connection getConnection() {
        return connections.computeIfAbsent(Thread.currentThread().threadId(),
                key ->
                {
                    try {
                        return dataSource.getConnection();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @Override
    public void close() {
        Optional.ofNullable(connections.remove(Thread.currentThread().threadId()))
                .ifPresent(conn -> {
                    try {
                        conn.close();
                    } catch (SQLException ignored) {
                    }
                });

    }

    public void closeAllConnections() {
        connections.values().forEach(conn -> {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
