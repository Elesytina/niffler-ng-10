package guru.qa.niffler.data.repository.userdata.impl;

import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.mapper.UdUserRowMapper;
import guru.qa.niffler.data.repository.userdata.UserdataUserRepository;
import guru.qa.niffler.data.tpl.DataSources;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.helper.TestConstantHolder.CFG;
import static guru.qa.niffler.model.enums.FriendshipStatus.ACCEPTED;
import static guru.qa.niffler.model.enums.FriendshipStatus.PENDING;
import static java.time.LocalDate.now;

public class UserdataUserRepositorySpringJdbcImpl implements UserdataUserRepository {
    private final JdbcTemplate template = new JdbcTemplate(DataSources.getDataSource(CFG.userdataJdbcUrl()));

    @Override
    public Optional<UserEntity> findById(UUID id) {
        return Optional.ofNullable(
                template.queryForObject("SELECT * FROM \"user\" WHERE id = ?",
                        UdUserRowMapper.INSTANCE, id)
        );
    }

    @Override
    public UserEntity create(UserEntity entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(conn -> {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO \"user\"( username, currency, firstname, surname, photo, photo_small, full_name) values (?,?,?, ?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, entity.getUsername());
            ps.setString(2, entity.getCurrency().name());
            ps.setString(3, entity.getFirstname());
            ps.setString(4, entity.getSurname());
            ps.setBytes(5, entity.getPhoto());
            ps.setBytes(6, entity.getPhotoSmall());
            ps.setString(7, entity.getFullname());

            return ps;
        }, keyHolder);

        final UUID id = (UUID) Objects.requireNonNull(keyHolder.getKeys()).get("id");
        entity.setId(id);

        return entity;
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
        template.update(conn -> {
            PreparedStatement ps = conn.prepareStatement("""
                    UPDATE friendship f
                    SET status = ?
                    WHERE f.requester_id in (?,?)
                    """);
            ps.setString(1, ACCEPTED.name());
            ps.setObject(2, requester.getId());
            ps.setObject(3, addressee.getId());

            if (ps.executeUpdate() == 0) {

                throw new RuntimeException("Failed to insert new invitation");
            }

            return ps;
        });
    }

    private void addInvitation(UserEntity requester, UserEntity addressee) {
        template.update(conn -> {
            PreparedStatement ps = conn.prepareStatement("""
                    INSERT INTO friendship
                    (requester_id, addressee_id, status, created_date)
                    values (?,?,?,?)
                    """);
            ps.setObject(1, requester.getId());
            ps.setObject(2, addressee.getId());
            ps.setString(3, PENDING.name());
            ps.setDate(4, java.sql.Date.valueOf(now()));

            return ps;
        });
    }
}
