package guru.qa.niffler.data.mapper;

import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthorityRowMapper implements RowMapper<AuthorityEntity> {

    public static final AuthorityRowMapper INSTANCE = new AuthorityRowMapper();

    @Override
    public AuthorityEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        AuthorityEntity authorityEntity = new AuthorityEntity();
        authorityEntity.setId(rs.getObject("id", UUID.class));
        authorityEntity.setUserId(rs.getObject("user_id", UUID.class));
        authorityEntity.setAuthority(rs.getString("authority"));

        return authorityEntity;
    }
}
