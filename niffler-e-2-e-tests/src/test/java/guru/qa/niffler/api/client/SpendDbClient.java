package guru.qa.niffler.service;

import guru.qa.niffler.api.client.CategoryClient;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.UUID;

public class SpendDbClient implements SpendClient {

    private static final Config CFG = Config.getInstance();
    private final CategoryClient categoryClient = new guru.qa.niffler.service.CategoryDbClient();

    @Override
    public SpendJson createSpend(SpendJson spend) {
        try {
            final JdbcTemplate jdbcTemplate = new JdbcTemplate(
                    new SingleConnectionDataSource(
                            DriverManager.getConnection(
                                    CFG.spendJdbcUrl(),
                                    "postgres",
                                    "secret"
                            ),
                            true
                    )
            );

            final KeyHolder kh = new GeneratedKeyHolder();
            final CategoryJson existingCategory = categoryClient.findCategoryByNameAndUsername(spend.category().name(), spend.username())
                    .orElseGet(() -> categoryClient.createCategory(spend.category()));

            jdbcTemplate.update(conn -> {
                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO \"spend\" (username, spend_date, currency, amount, description, category_id) " +
                                "VALUES (?, ?, ?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS
                );
                ps.setString(1, spend.username());
                ps.setDate(2, new java.sql.Date(spend.spendDate().getTime()));
                ps.setString(3, spend.currency().name());
                ps.setDouble(4, spend.amount());
                ps.setString(5, spend.description());
                ps.setObject(6, existingCategory.id());
                return ps;
            }, kh);

            return new SpendJson(
                    (UUID) kh.getKeys().get("id"),
                    spend.spendDate(),
                    existingCategory,
                    spend.currency(),
                    spend.amount(),
                    spend.description(),
                    spend.username()
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SpendJson getSpendById(UUID spendId, String userName) {
        throw new UnsupportedOperationException("Not implemented :(");
    }

    @Override
    public List<SpendJson> getAllSpends(String username) {
        throw new UnsupportedOperationException("Not implemented :(");
    }

    @Override
    public SpendJson updateSpend(SpendJson spend) {
        throw new UnsupportedOperationException("Not implemented :(");
    }

    @Override
    public SpendJson deleteSpend(SpendJson spendJson) {
        throw new UnsupportedOperationException("Not implemented :(");
    }
}
