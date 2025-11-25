package guru.qa.niffler.data.mapper;

import guru.qa.niffler.data.entity.spend.CategoryEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryRowMapper implements RowMapper<CategoryEntity> {

    public static CategoryRowMapper INSTANCE = new CategoryRowMapper();

    @Override
    public CategoryEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        CategoryEntity category = new CategoryEntity();
        category.setId(rs.getObject("id", UUID.class));
        category.setName(rs.getString("name"));
        category.setUsername(rs.getString("username"));
        category.setArchived(rs.getBoolean("archived"));

        return category;
    }
}
