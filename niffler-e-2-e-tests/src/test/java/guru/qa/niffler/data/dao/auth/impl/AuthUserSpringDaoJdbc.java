package guru.qa.niffler.data.dao.auth.impl;

import guru.qa.niffler.data.dao.auth.AuthUserDao;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.mapper.AuthUserRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class AuthUserSpringDaoJdbc implements AuthUserDao {

    private final DataSource dataSource;
    private static final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();


    @Override
    public Optional<AuthUserEntity> findByUsername(String username) {
        JdbcTemplate template = new JdbcTemplate(dataSource);

        return Optional.ofNullable(
                template.queryForObject("SELECT * FROM \"user\" WHERE username = ?",
                        AuthUserRowMapper.INSTANCE, username)
        );
    }

    @Override
    public AuthUserEntity create(AuthUserEntity authUser) {
        JdbcTemplate template = new JdbcTemplate(dataSource);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(conn -> {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO \"user\"( username, password,enabled, account_non_expired, account_non_locked, credentials_non_expired) values (?,?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, authUser.getUsername());
            ps.setString(2, passwordEncoder.encode(authUser.getPassword()));
            ps.setBoolean(3, authUser.getEnabled());
            ps.setBoolean(4, authUser.getAccountNonExpired());
            ps.setBoolean(5, authUser.getAccountNonLocked());
            ps.setBoolean(6, authUser.getCredentialsNonExpired());

            return ps;
        }, keyHolder);

        final UUID id = (UUID) Objects.requireNonNull(keyHolder.getKeys()).get("id");
        authUser.setId(id);

        return authUser;
    }
}
