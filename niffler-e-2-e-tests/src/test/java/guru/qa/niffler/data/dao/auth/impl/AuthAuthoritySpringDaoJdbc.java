package guru.qa.niffler.data.dao.auth.impl;

import guru.qa.niffler.data.dao.auth.AuthAuthorityDao;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.mapper.AuthorityRowMapper;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class AuthAuthoritySpringDaoJdbc implements AuthAuthorityDao {

    private final DataSource dataSource;

    @Override
    public List<AuthorityEntity> findAllByUserId(UUID userId) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        return jdbcTemplate.query("SELECT * FROM authority where user_id = ?",
                AuthorityRowMapper.INSTANCE,
                userId);
    }

    @Override
    public void create(List<AuthorityEntity> authorities) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.batchUpdate("INSERT INTO authority(user_id, authority)  VALUES( ?, ?)",
                new BatchPreparedStatementSetter() {

                    @Override
                    public void setValues(@NotNull PreparedStatement ps, int i) throws SQLException {
                        ps.setObject(1, authorities.get(i).getUserId());
                        ps.setString(2, authorities.get(i).getAuthority());
                    }

                    @Override
                    public int getBatchSize() {
                        return authorities.size();
                    }
                });
    }
}
