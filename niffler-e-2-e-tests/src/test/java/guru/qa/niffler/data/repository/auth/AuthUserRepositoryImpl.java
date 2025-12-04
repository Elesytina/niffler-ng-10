package guru.qa.niffler.data.repository.auth;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.mapper.AuthUserRowMapper;
import guru.qa.niffler.data.mapper.AuthorityRowMapper;
import guru.qa.niffler.data.tpl.JdbcConnectionHolder;
import guru.qa.niffler.model.enums.Authority;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

import static guru.qa.niffler.data.tpl.Connections.getHolder;
import static guru.qa.niffler.helper.TestConstantHolder.CFG;
import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class AuthUserRepositoryImpl implements AuthUserRepository {
    private final JdbcConnectionHolder connectionHolder = getHolder(CFG.authJdbcUrl());
    private static final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Override
    public Optional<AuthUserEntity> findByUsername(String username) {
        try (PreparedStatement ps = connectionHolder.getConnection()
                .prepareStatement("""
                        SELECT *
                        FROM "user" u
                        JOIN authority a
                        ON u.id = a.user_id
                        WHERE u.username = ?
                        """
                )) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            List<AuthorityEntity> authorities = new ArrayList<>();
            AuthUserEntity authUserEntity = null;
            while (rs.next()) {

                if (authUserEntity == null) {
                    authUserEntity = AuthUserRowMapper.INSTANCE.mapRow(rs, 1);

                }
                AuthorityEntity authorityEntity = AuthorityRowMapper.INSTANCE.mapRow(rs, 1);
                authorities.add(authorityEntity);
            }

            if (authUserEntity != null) {
                authUserEntity.setAuthorities(authorities);

                return Optional.of(authUserEntity);
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AuthUserEntity create(AuthUserEntity entity) {
        try (var userPs = connectionHolder.getConnection()
                .prepareStatement("""
                        INSERT INTO "user"
                        (username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired)
                        values (?,?,?,?,?,?)
                        """, RETURN_GENERATED_KEYS);
             var authPs = connectionHolder.getConnection()
                     .prepareStatement("""
                             INSERT INTO authority(user_id, authority)
                             values (?,?)
                             """, RETURN_GENERATED_KEYS);) {
            userPs.setString(1, entity.getUsername());
            userPs.setString(2, passwordEncoder.encode(entity.getPassword()));
            userPs.setBoolean(3, entity.getEnabled());
            userPs.setBoolean(4, entity.getAccountNonExpired());
            userPs.setBoolean(5, entity.getAccountNonLocked());
            userPs.setBoolean(6, entity.getCredentialsNonExpired());

            if (userPs.executeUpdate() != 0) {
                ResultSet resultSet = userPs.getGeneratedKeys();

                if (resultSet.next()) {
                    entity.setId(resultSet.getObject(1, UUID.class));
                }

                List<AuthorityEntity> authorities = new ArrayList<>();

                for (Authority authority : Authority.values()) {
                    authPs.setObject(1, entity.getId());
                    authPs.setString(2, authority.name());
                }
                int[] updatedCount = authPs.executeBatch();
                IntStream.range(0, updatedCount.length).forEach(i -> {
                    AuthorityEntity authorityEntity = new AuthorityEntity();
                    authorityEntity.setAuthority(Authority.values()[updatedCount[i]]);
                    authorityEntity.setUser(entity);
                    authorities.add(authorityEntity);
                });
                entity.setAuthorities(authorities);

                return entity;
            }
            throw new RuntimeException("Failed to insert new  auth user and authorities");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
