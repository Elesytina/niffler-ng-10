package guru.qa.niffler.data.repository.auth.impl;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.mapper.extractor.AuthUserResultSetExtractor;
import guru.qa.niffler.data.repository.auth.AuthUserRepository;
import guru.qa.niffler.data.tpl.JdbcConnectionHolder;

import javax.annotation.ParametersAreNonnullByDefault;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.getHolder;
import static guru.qa.niffler.helper.TestConstantHolder.CFG;
import static guru.qa.niffler.helper.TestConstantHolder.PASSWORD_ENCODER;
import static java.sql.Statement.RETURN_GENERATED_KEYS;
@ParametersAreNonnullByDefault
public class AuthUserRepositoryJdbcImpl implements AuthUserRepository {

    private final JdbcConnectionHolder connectionHolder = getHolder(CFG.authJdbcUrl());

    @Override
    public AuthUserEntity create(AuthUserEntity userEntity) {
        try (PreparedStatement userPs = connectionHolder.getConnection()
                .prepareStatement("""
                        INSERT INTO "user"
                        (username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired)
                        values (?,?,?,?,?,?)
                        """, RETURN_GENERATED_KEYS);
             PreparedStatement authorityPs = connectionHolder.getConnection()
                     .prepareStatement("INSERT INTO authority(user_id, authority) values (?,?)")) {
            userPs.setString(1, userEntity.getUsername());
            userPs.setString(2, userEntity.getPassword());
            userPs.setBoolean(3, userEntity.getEnabled());
            userPs.setBoolean(4, userEntity.getAccountNonExpired());
            userPs.setBoolean(5, userEntity.getAccountNonLocked());
            userPs.setBoolean(6, userEntity.getCredentialsNonExpired());

            if (userPs.executeUpdate() != 0) {
                ResultSet rs = userPs.getGeneratedKeys();

                if (rs.next()) {
                    userEntity.setId(rs.getObject(1, UUID.class));
                }
            } else {
                throw new SQLException("Failed to insert user");
            }

            List<AuthorityEntity> authorities = userEntity.getAuthorities();

            for (AuthorityEntity entity : authorities) {
                authorityPs.setObject(1, userEntity.getId());
                authorityPs.setString(2, entity.getAuthority().name());
                authorityPs.addBatch();
                authorityPs.clearParameters();
            }
            authorityPs.executeBatch();
            userEntity.setAuthorities(authorities);

            return userEntity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AuthUserEntity update(AuthUserEntity userEntity) {
        try (PreparedStatement userPs = connectionHolder.getConnection()
                .prepareStatement("""
                        UPDATE "user"
                        SET username=?,
                         password=?,
                         enabled=?,
                         account_non_expired=?,
                         account_non_locked=?,
                         credentials_non_expired=?
                         WHERE id=?
                        """)) {
            userPs.setString(1, userEntity.getUsername());
            userPs.setString(2, PASSWORD_ENCODER.encode(userEntity.getPassword()));
            userPs.setBoolean(3, userEntity.getEnabled());
            userPs.setBoolean(4, userEntity.getAccountNonExpired());
            userPs.setBoolean(5, userEntity.getAccountNonLocked());
            userPs.setBoolean(6, userEntity.getCredentialsNonExpired());
            userPs.setObject(7, userEntity.getId());

            if (userPs.executeUpdate() == 0) {
                throw new SQLException("Failed to insert user");
            }
            return userEntity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<AuthUserEntity> findById(UUID id) {
        try (PreparedStatement ps = connectionHolder.getConnection()
                .prepareStatement("""
                        SELECT
                        u.id AS id,
                        u.username AS username,
                        u.password AS password,
                        u.enabled AS enabled,
                        u.credentials_non_expired AS credentials_non_expired,
                        u.account_non_locked AS account_non_locked,
                        u.account_non_expired AS account_non_expired,
                        a.id AS authority_id,
                        a.user_id AS user_id,
                        a.authority AS authority
                        
                        FROM "user" u
                        JOIN authority  a
                        ON u.id = a.user_id
                        where user_id = ?
                        """)) {
            ps.setObject(1, id);
            ResultSet rs = ps.executeQuery();

            return Optional.ofNullable(AuthUserResultSetExtractor.INSTANCE.extractData(rs));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<AuthUserEntity> findByUsername(String username) {
        try (PreparedStatement ps = connectionHolder.getConnection()
                .prepareStatement("""
                        SELECT
                        u.id AS id,
                        u.username AS username,
                        u.password AS password,
                        u.enabled AS enabled,
                        u.credentials_non_expired AS credentials_non_expired,
                        u.account_non_locked AS account_non_locked,
                        u.account_non_expired AS account_non_expired,
                        a.id AS authority_id,
                        a.user_id AS user_id,
                        a.authority AS authority
                        
                        FROM "user" u
                        JOIN authority  a
                        ON u.id = a.user_id
                        where username = ?
                        """)) {
            ps.setObject(1, username);
            ResultSet rs = ps.executeQuery();

            return Optional.ofNullable(AuthUserResultSetExtractor.INSTANCE.extractData(rs));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(UUID id) {
        Optional<AuthUserEntity> authUser = findById(id);

        if (authUser.isPresent()) {
            try (PreparedStatement userPs = connectionHolder.getConnection()
                    .prepareStatement("""
                            DELETE FROM "user"
                            WHERE id = ?
                            """);
                 PreparedStatement authorityPs = connectionHolder.getConnection()
                         .prepareStatement("DELETE FROM authority WHERE user_id = ?")) {
                userPs.setObject(1, id);
                authorityPs.setObject(1, id);

                if (authorityPs.executeUpdate() == 0) {

                    throw new SQLException("Failed to delete authorities with user id %s".formatted(id));
                }

                if (userPs.executeUpdate() == 0) {

                    throw new SQLException("Failed to delete auth user with id %s".formatted(id));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException("AuthUser with id %s not found".formatted(id));
        }
    }
}
