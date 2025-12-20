package guru.qa.niffler.data.dao.auth.impl;

import guru.qa.niffler.data.dao.auth.AuthUserDao;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.tpl.JdbcConnectionHolder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.getHolder;
import static guru.qa.niffler.helper.TestConstantHolder.CFG;
import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class AuthUserDaoJdbc implements AuthUserDao {
    private final JdbcConnectionHolder connectionHolder = getHolder(CFG.authJdbcUrl());
    private static final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Override
    public Optional<AuthUserEntity> findByUsername(String username) {
        try (PreparedStatement ps = connectionHolder.getConnection()
                .prepareStatement("SELECT * FROM \"user\" WHERE username = ?")) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            return getAuthUserEntity(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AuthUserEntity create(AuthUserEntity entity) {
        try (PreparedStatement ps = connectionHolder.getConnection()
                .prepareStatement("INSERT INTO \"user\"( username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) values (?,?,?,?,?,?)",
                        RETURN_GENERATED_KEYS)) {
            ps.setString(1, entity.getUsername());
            ps.setString(2, passwordEncoder.encode(entity.getPassword()));
            ps.setBoolean(3, entity.getEnabled());
            ps.setBoolean(4, entity.getAccountNonExpired());
            ps.setBoolean(5, entity.getAccountNonLocked());
            ps.setBoolean(6, entity.getCredentialsNonExpired());

            if (ps.executeUpdate() != 0) {
                ResultSet resultSet = ps.getGeneratedKeys();

                if (resultSet.next()) {
                    entity.setId(resultSet.getObject(1, UUID.class));
                }

                return entity;
            }
            throw new RuntimeException("Failed to insert new user");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private Optional<AuthUserEntity> getAuthUserEntity(ResultSet rs) throws SQLException {
        if (rs.next()) {
            AuthUserEntity userEntity = new AuthUserEntity();
            userEntity.setId(rs.getObject("id", UUID.class));
            userEntity.setUsername(rs.getString("username"));
            userEntity.setPassword(rs.getString("password"));
            userEntity.setEnabled(rs.getBoolean("enabled"));
            userEntity.setAccountNonExpired(rs.getBoolean("account_non_expired"));
            userEntity.setAccountNonLocked(rs.getBoolean("account_non_locked"));
            userEntity.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));

            return Optional.of(userEntity);
        }

        return Optional.empty();
    }
}
