package guru.qa.niffler.service;

import guru.qa.niffler.api.client.CategoryClient;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.CategoryJson;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public class CategoryDbClient implements CategoryClient {

    private static final Config CFG = Config.getInstance();

    @Override
    public CategoryJson createCategory(CategoryJson category) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CategoryJson updateCategory(CategoryJson categoryJson) {
        throw new UnsupportedOperationException("Not implemented :(");
    }

    @Override
    public Optional<CategoryJson> findCategoryByNameAndUsername(String categoryName, String username) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<CategoryJson> getAllCategories(String username) {
        throw new UnsupportedOperationException("Not implemented :(");
    }
}
