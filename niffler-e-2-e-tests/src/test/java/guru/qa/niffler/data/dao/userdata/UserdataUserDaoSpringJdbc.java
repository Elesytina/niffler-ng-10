package guru.qa.niffler.data.dao.userdata;

import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.mapper.UdUserRowMapper;
import guru.qa.niffler.data.tpl.DataSources;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.DataSources.*;
import static guru.qa.niffler.helper.TestConstantHolder.CFG;

@RequiredArgsConstructor
public class UserdataUserDaoSpringJdbc implements UserdataUserDao {

    private final JdbcTemplate template = new JdbcTemplate(getDataSource(CFG.userdataJdbcUrl()));

    @Override
    public Optional<UserEntity> findById(UUID id) {

        return Optional.ofNullable(
                template.queryForObject("SELECT * FROM \"user\" WHERE id = ?",
                        UdUserRowMapper.INSTANCE, id)
        );
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {

        return Optional.ofNullable(
                template.queryForObject("SELECT * FROM \"user\" WHERE username = ?",
                        UdUserRowMapper.INSTANCE, username)
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
    public boolean delete(UserEntity spendEntity) {
        int result = template.update("DELETE FROM \"user\" WHERE id = ?", spendEntity.getId());

        return result == 1;
    }
}
