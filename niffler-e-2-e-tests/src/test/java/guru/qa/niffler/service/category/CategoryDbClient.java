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

}
