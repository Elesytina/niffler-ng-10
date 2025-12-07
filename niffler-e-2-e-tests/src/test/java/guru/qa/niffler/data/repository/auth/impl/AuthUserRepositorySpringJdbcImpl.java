package guru.qa.niffler.data.repository.auth.impl;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.mapper.extractor.AuthUserResultSetExtractor;
import guru.qa.niffler.data.repository.auth.AuthUserRepository;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.DataSources.getDataSource;
import static guru.qa.niffler.helper.TestConstantHolder.CFG;
import static guru.qa.niffler.helper.TestConstantHolder.PASSWORD_ENCODER;

public class AuthUserRepositorySpringJdbcImpl implements AuthUserRepository {
    private final JdbcTemplate template = new JdbcTemplate(getDataSource(CFG.authJdbcUrl()));
    private final JdbcTransactionTemplate txTemplate = new JdbcTransactionTemplate(CFG.authJdbcUrl());

    @Override
    public Optional<AuthUserEntity> findById(UUID userId) {
        AuthUserEntity userEntity = template.query("""
                        SELECT * FROM "user" u
                        JOIN authority  a
                        ON u.id = a.user_id
                        where user_id = ?
                        """,
                AuthUserResultSetExtractor.INSTANCE,
                userId);

        return Optional.ofNullable(userEntity);
    }

    @Override
    public AuthUserEntity create(AuthUserEntity authUser) {
        return txTemplate.execute(() -> {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            template.update(conn -> {
                PreparedStatement ps = conn.prepareStatement(
                        """
                                INSERT INTO "user"( username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) values (?,?,?,?,?,?)
                                """, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, authUser.getUsername());
                ps.setString(2, PASSWORD_ENCODER.encode(authUser.getPassword()));
                ps.setBoolean(3, authUser.getEnabled());
                ps.setBoolean(4, authUser.getAccountNonExpired());
                ps.setBoolean(5, authUser.getAccountNonLocked());
                ps.setBoolean(6, authUser.getCredentialsNonExpired());

                return ps;
            }, keyHolder);

            final UUID id = (UUID) Objects.requireNonNull(keyHolder.getKeys()).get("id");
            authUser.setId(id);

            List<AuthorityEntity> authorities = authUser.getAuthorities();
            template.batchUpdate("INSERT INTO authority(user_id, authority)  VALUES( ?, ?)",
                    new BatchPreparedStatementSetter() {

                        @Override
                        public void setValues(
                                @NotNull PreparedStatement ps, int i) throws SQLException {
                            ps.setObject(1, authorities.get(i).getUser().getId());
                            ps.setString(2, authorities.get(i).getAuthority().name());
                        }

                        @Override
                        public int getBatchSize() {
                            return authorities.size();
                        }
                    });
            authUser.setAuthorities(authorities);

            return authUser;
        });
    }
}
