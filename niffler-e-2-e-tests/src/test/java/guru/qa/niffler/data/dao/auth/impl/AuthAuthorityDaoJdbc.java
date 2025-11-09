package guru.qa.niffler.data.dao.auth.impl;

import guru.qa.niffler.data.dao.auth.AuthAuthorityDao;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.model.enums.Authority;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class AuthAuthorityDaoJdbc implements AuthAuthorityDao {

    private final Connection connection;

    public AuthAuthorityDaoJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<AuthorityEntity> findAllByUserId(UUID userId) {
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM authority WHERE user_id = ?")) {
            ps.setObject(1, userId);
            ResultSet rs = ps.executeQuery();
            List<AuthorityEntity> authorities = new ArrayList<>();

            while (rs.next()) {
                AuthorityEntity entity = new AuthorityEntity();
                entity.setId(rs.getObject("id", UUID.class));
                entity.setUserId(userId);
                entity.setAuthority(rs.getObject("authority", Authority.class));

                authorities.add(entity);
            }

            return authorities;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<AuthorityEntity> addAll(List<AuthorityEntity> entities) {
        List<AuthorityEntity> result = new ArrayList<>();
        for (AuthorityEntity entity : entities) {
            try (PreparedStatement ps = connection.prepareStatement("INSERT INTO authority(user_id, authority) values (?,?)",
                    RETURN_GENERATED_KEYS)) {
                ps.setObject(1, entity.getUserId());
                ps.setString(2, entity.getAuthority().name());

                if (ps.executeUpdate() != 0) {
                    ResultSet resultSet = ps.getGeneratedKeys();

                    if (resultSet.next()) {
                        entity.setId(resultSet.getObject(1, UUID.class));
                        result.add(entity);
                    } else {
                        throw new RuntimeException("Failed to insert new authority");
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }
}
