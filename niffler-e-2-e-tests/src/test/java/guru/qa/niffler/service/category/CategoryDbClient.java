package guru.qa.niffler.service.category;

import guru.qa.niffler.model.CategoryJson;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.sql.Statement.RETURN_GENERATED_KEYS;


public class CategoryDbClient implements CategoryClient {

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

//    public List<AccountModel> findAllByCustomerId(int customerId) {
//        List<AccountModel> accounts = new ArrayList<>();
//        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(
//                "select * from account a where a.customer_id = ?")) {
//            ps.setInt(1, customerId);
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()) {
//                AccountModel accountModel = new AccountModel();
//                accountModel.setId(rs.getInt("id"));
//                accountModel.setName(rs.getString("name"));
//                accountModel.setCustomerId(customerId);
//                accountModel.setBalance(rs.getBigDecimal("balance"));
//                accounts.add(accountModel);
//            }
//            return accounts;
//        } catch (SQLException ex) {
//            throw new DaoLayerException("Error of sql query in findAllByCustomerId!");
//        }
//    }
//
//    public AccountModel create(int customerId, String name, BigDecimal balance) {
//        try (Connection conn = dataSource.getConnection();
//             PreparedStatement ps = conn.prepareStatement("insert into account" +
//                             "(name, customer_id, balance) values (?, ?, ?);",
//                     RETURN_GENERATED_KEYS)) {
//            ps.setString(1, name);
//            ps.setInt(2, customerId);
//            ps.setBigDecimal(3, balance);
//            AccountModel accountModel;
//            if (ps.executeUpdate() != 0) {
//                ResultSet rs = ps.getGeneratedKeys();
//                if (rs.next()) {
//                    accountModel = new AccountModel();
//                    accountModel.setId(rs.getInt(1));
//                    accountModel.setName(name);
//                    accountModel.setBalance(balance);
//                    accountModel.setCustomerId(customerId);
//                } else {
//                    throw new DaoLayerException("Account model wasn't create");
//                }
//            } else {
//                throw new DaoLayerException("Account model wasn't created");
//            }
//            return accountModel;
//        } catch (SQLException ex) {
//            throw new DaoLayerException(ex);
//        }
//    }
//
//    public boolean delete(int customerId, int id) {
//        try (Connection conn = dataSource.getConnection();
//             PreparedStatement s = conn.prepareStatement("delete from account " +
//                     " where id = ? and customer_id = ?;")) {
//            s.setInt(1, id);
//            s.setInt(2, customerId);
//            return s.executeUpdate() == 1;
//        } catch (SQLException ex) {
//            throw new DaoLayerException("Deleting failed!");
//        }
//    }

//
//    public class ConnectionManager {
//
//        private ConnectionManager() {
//        }
//
//        public Connection getConnection() {
//            try {
//                DataSource dataSource = getDataSource();
//                return dataSource.getConnection();
//            } catch (SQLException ex) {
//                throw new ConnectionException("Data base connection error!");
//            }
//        }
//
//        public static ConnectionManager getInstance() {
//            return InstanceHolder.connectionManager;
//        }
//
//        public static DataSource getDataSource() {
//            return InstanceHolder.dataSource;
//        }
//
//        private static HikariConfig getConfig() {
//            HikariConfig config = new HikariConfig();
//            config.setJdbcUrl("jdbc:postgresql://localhost:5432/postgres");
//            config.setUsername("postgres");
//            config.setPassword("111");
//            return config;
//        }
//
//        private static class InstanceHolder {
//            public static final DataSource dataSource = new HikariDataSource(getConfig());
//            public static final ConnectionManager connectionManager = new ConnectionManager();
//        }
//
//    }
}
