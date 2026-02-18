package guru.qa.niffler.data.tpl;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.p6spy.engine.spy.P6DataSource;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

@ParametersAreNonnullByDefault
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DataSources {

    private final static Map<String, DataSource> dataSources = new ConcurrentHashMap<>();

    public static DataSource getDataSource(String jdbcUrl) {
        final String dbName = StringUtils.substringAfter(jdbcUrl, "5432/");

        return dataSources.computeIfAbsent(jdbcUrl,
                key -> {
                    Properties properties = new Properties();
                    properties.setProperty("URL", jdbcUrl);
                    properties.setProperty("user", "postgres");
                    properties.setProperty("password", "secret");

                    AtomikosDataSourceBean datasourceBean = new AtomikosDataSourceBean();
                    datasourceBean.setUniqueResourceName(dbName);
                    datasourceBean.setXaDataSourceClassName("org.postgresql.xa.PGXADataSource");
                    datasourceBean.setXaProperties(properties);
                    datasourceBean.setPoolSize(4);
                    datasourceBean.setMaxPoolSize(10);
                    P6DataSource p6DataSource = new P6DataSource(datasourceBean);
                    try {
                        InitialContext ctx = new InitialContext();
                        ctx.bind("java:comp/env/jdbc/%s".formatted(dbName), p6DataSource);
                    } catch (NamingException e) {
                        throw new RuntimeException(e);
                    }

                    return datasourceBean;
                });
    }
}
