package guru.qa.niffler.data;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Databases {

    private final static Map<String, DataSource> datasources = new ConcurrentHashMap<>();

    public static Connection getConnection(String jdbcUrl) throws SQLException {
        return getDataSource(jdbcUrl).getConnection();
    }

    private static DataSource getDataSource(String jdbcUrl) {
        return datasources.computeIfAbsent(jdbcUrl,
                key -> {
                    PGSimpleDataSource dataSource = new PGSimpleDataSource();
                    dataSource.setUser("postgres");
                    dataSource.setPassword("secret");
                    dataSource.setUrl(key);
                    return dataSource;
                });
    }

}
