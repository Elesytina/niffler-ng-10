package guru.qa.niffler.data.mapper;

import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.enums.CurrencyValues;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SpendRowMapper implements RowMapper<SpendEntity> {

    public static final RowMapper<SpendEntity> INSTANCE = new SpendRowMapper();

    @Override
    public SpendEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        SpendEntity entity = new SpendEntity();
        entity.setId(rs.getObject("id", UUID.class));
        entity.setUsername(rs.getString("username"));
        entity.setSpendDate(rs.getDate("spend_date"));
        entity.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
        entity.setAmount(rs.getDouble("amount"));
        entity.setDescription(rs.getString("description"));

        CategoryEntity category = new CategoryEntity();
        category.setId(rs.getObject("category_id", UUID.class));
        category.setName(rs.getString("name"));
        category.setUsername(rs.getString("username"));
        category.setArchived(rs.getBoolean("archived"));
        entity.setCategory(category);

        return entity;
    }
}
