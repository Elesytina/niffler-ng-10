package guru.qa.niffler.data.dao.auth.impl;

import guru.qa.niffler.data.dao.auth.AuthAuthorityDao;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.mapper.AuthorityRowMapper;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.ParametersAreNonnullByDefault;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.DataSources.getDataSource;
import static guru.qa.niffler.helper.TestConstantHolder.CFG;

@ParametersAreNonnullByDefault
@RequiredArgsConstructor
public class AuthAuthoritySpringDaoJdbc implements AuthAuthorityDao {
    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource(CFG.authJdbcUrl()));

    @Override
    public @NotNull List<AuthorityEntity> findAllByUserId(UUID userId) {

        return jdbcTemplate.query("SELECT * FROM authority where user_id = ?",
                AuthorityRowMapper.INSTANCE,
                userId);
    }

    @Override
    public void create(List<AuthorityEntity> authorities) {
        jdbcTemplate.batchUpdate("INSERT INTO authority(user_id, authority)  VALUES( ?, ?)",
                new BatchPreparedStatementSetter() {

                    @Override
                    public void setValues(@NotNull PreparedStatement ps, int i) throws SQLException {
                        ps.setObject(1, authorities.get(i).getUser().getId());
                        ps.setString(2, authorities.get(i).getAuthority().name());
                    }

                    @Override
                    public int getBatchSize() {
                        return authorities.size();
                    }
                });
    }

}
