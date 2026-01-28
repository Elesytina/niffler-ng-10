package guru.qa.niffler.data.mapper.extractor;

import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.enums.CurrencyValues;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SpendResultSetExtractor implements ResultSetExtractor<List<SpendEntity>> {

    public static final SpendResultSetExtractor INSTANCE = new SpendResultSetExtractor();

    @Override
    public List<SpendEntity> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<UUID, SpendEntity> spendMap = new ConcurrentHashMap<>();
        List<SpendEntity> spendEntities = new ArrayList<>();

        while (rs.next()) {
            UUID id = rs.getObject("id", UUID.class);
            var spend = spendMap.computeIfAbsent(id,
                    spendId -> {
                        try {
                            var spendEntity = new SpendEntity();
                            spendEntity.setId(spendId);
                            spendEntity.setUsername(rs.getString("username"));
                            spendEntity.setAmount(rs.getDouble("amount"));
                            spendEntity.setDescription(rs.getString("description"));
                            spendEntity.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
                            spendEntity.setSpendDate(rs.getDate("spend_date"));

                            CategoryEntity category = new CategoryEntity();
                            category.setId(rs.getObject("category_id", UUID.class));
                            category.setArchived(rs.getBoolean("archived"));
                            category.setUsername(rs.getString("username"));
                            spendEntity.setCategory(category);

                            return spendEntity;
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    });
            spendEntities.add(spend);
        }

        return spendEntities;
    }
}
