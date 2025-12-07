package guru.qa.niffler.data.repository.userdata.impl;

import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.userdata.UserdataUserRepository;
import guru.qa.niffler.data.tpl.JdbcConnectionHolder;
import guru.qa.niffler.model.enums.CurrencyValues;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.getHolder;
import static guru.qa.niffler.helper.TestConstantHolder.CFG;
import static guru.qa.niffler.model.enums.FriendshipStatus.ACCEPTED;
import static guru.qa.niffler.model.enums.FriendshipStatus.PENDING;
import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.time.LocalDate.now;


public class UserdataUserRepositoryJdbcImpl implements UserdataUserRepository {
    private final JdbcConnectionHolder connectionHolder = getHolder(CFG.userdataJdbcUrl());

    @Override
    public Optional<UserEntity> findById(UUID id) {
        try (PreparedStatement ps = connectionHolder.getConnection()
                .prepareStatement("SELECT * FROM \"user\" WHERE id = ?")) {
            ps.setObject(1, id);
            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                UserEntity entity = getUserEntity(resultSet);

                return Optional.of(entity);
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserEntity create(UserEntity entity) {
        try (PreparedStatement ps = connectionHolder.getConnection()
                .prepareStatement("""
                        INSERT INTO "user"
                        (username, currency, firstname, surname, photo, photo_small, full_name)
                        values (?,?,?, ?,?,?,?)
                        """, RETURN_GENERATED_KEYS)) {
            ps.setString(1, entity.getUsername());
            ps.setString(2, entity.getCurrency().name());
            ps.setString(3, entity.getFirstname());
            ps.setString(4, entity.getSurname());
            ps.setBytes(5, entity.getPhoto());
            ps.setBytes(6, entity.getPhotoSmall());
            ps.setString(7, entity.getFullname());

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

    @Override
    public void addIncomeInvitation(UserEntity requester, UserEntity addressee) {
        addInvitation(addressee, requester);
    }

    @Override
    public void addOutcomeInvitation(UserEntity requester, UserEntity addressee) {
        addInvitation(requester, addressee);
    }

    @Override
    public void addFriend(UserEntity requester, UserEntity addressee) {
        try (PreparedStatement ps = connectionHolder.getConnection()
                .prepareStatement("""
                        UPDATE friendship f
                        SET status = ?
                        WHERE f.requester_id in (?,?)
                        """)) {
            ps.setString(1, ACCEPTED.name());
            ps.setObject(2, requester.getId());
            ps.setObject(3, addressee.getId());

            if (ps.executeUpdate() == 0) {

                throw new RuntimeException("Failed to update friendship");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void addInvitation(UserEntity requester, UserEntity addressee) {
        try (PreparedStatement ps = connectionHolder.getConnection()
                .prepareStatement("""
                        INSERT INTO friendship
                        (requester_id, addressee_id, status, created_date)
                        values (?,?,?,?)
                        """)) {
            ps.setObject(1, requester.getId());
            ps.setObject(2, addressee.getId());
            ps.setString(3, PENDING.name());
            ps.setDate(4, java.sql.Date.valueOf(now()));

            if (ps.executeUpdate() == 0) {

                throw new RuntimeException("Failed to insert new invitation");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private UserEntity getUserEntity(ResultSet resultSet) throws SQLException {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(resultSet.getObject("id", UUID.class));
        userEntity.setFullname(resultSet.getString("full_name"));
        userEntity.setUsername(resultSet.getString("username"));
        userEntity.setFirstname(resultSet.getString("firstname"));
        userEntity.setSurname(resultSet.getString("surname"));
        userEntity.setCurrency(CurrencyValues.valueOf(resultSet.getString("currency")));
        userEntity.setPhoto(resultSet.getBytes("photo"));
        userEntity.setPhotoSmall(resultSet.getBytes("photo_small"));

        return userEntity;
    }
}
