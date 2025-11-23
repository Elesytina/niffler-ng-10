package guru.qa.niffler.data.tpl;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DataSources {

    private final static Map<String, DataSource> dataSources = new ConcurrentHashMap<>();

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
                    datasourceBean.setPoolSize(4);
                    datasourceBean.setMaxPoolSize(10);

                    return datasourceBean;
                });
    }
}
