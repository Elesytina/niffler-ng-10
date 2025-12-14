package guru.qa.niffler.data.dao.auth.impl;

import guru.qa.niffler.data.dao.auth.AuthAuthorityDao;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.tpl.JdbcConnectionHolder;
import guru.qa.niffler.model.enums.Authority;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.getHolder;
import static guru.qa.niffler.helper.TestConstantHolder.CFG;

public class AuthAuthorityDaoJdbc implements AuthAuthorityDao {
    private final JdbcConnectionHolder connectionHolder = getHolder(CFG.authJdbcUrl());

    @Override
    public List<AuthorityEntity> findAllByUserId(UUID userId) {
        try (PreparedStatement ps = connectionHolder.getConnection()
                .prepareStatement("SELECT * FROM authority WHERE user_id = ?")) {
            ps.setObject(1, userId);
            ResultSet rs = ps.executeQuery();
            List<AuthorityEntity> authorities = new ArrayList<>();

            while (rs.next()) {
                AuthorityEntity entity = new AuthorityEntity();
                entity.setId(rs.getObject("id", UUID.class));
                AuthUserEntity user = new AuthUserEntity();
                user.setId(userId);
                entity.setUser(user);
                entity.setAuthority(Authority.valueOf(rs.getString("authority")));

                authorities.add(entity);
            }

            return authorities;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void create(List<AuthorityEntity> entities) {
        try (PreparedStatement ps = connectionHolder.getConnection()
                .prepareStatement("INSERT INTO authority(user_id, authority) values (?,?)")) {

            for (AuthorityEntity entity : entities) {
                ps.setObject(1, entity.getUser().getId());
                ps.setString(2, entity.getAuthority().name());
                ps.addBatch();
                ps.clearParameters();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}


