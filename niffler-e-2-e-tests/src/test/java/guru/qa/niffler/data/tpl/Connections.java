package guru.qa.niffler.data.tpl;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Connections {

    private static final Map<String, JdbcConnectionHolder> holders = new ConcurrentHashMap<>();

    public static JdbcConnectionHolder getHolder(String jdbcUrl) {

        return holders.computeIfAbsent(jdbcUrl,
                key ->
                        new JdbcConnectionHolder(DataSources.getDataSource(jdbcUrl))
        );
    }

    public static JdbcConnectionHolders getHolders(String... jdbcUrl) {
        List<JdbcConnectionHolder> holdersList = new ArrayList<>();

        for (String url : jdbcUrl) {
            holdersList.add(getHolder(url));
        }

        return new JdbcConnectionHolders(holdersList);
    }

    public static void closeAllConnections() {
        holders.values().forEach(JdbcConnectionHolder::closeAllConnections);
    }
}
