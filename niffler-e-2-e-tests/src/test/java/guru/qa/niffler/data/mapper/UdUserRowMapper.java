package guru.qa.niffler.data.mapper;

import guru.qa.niffler.data.entity.userdata.UserEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UdUserRowMapper implements RowMapper<UserEntity> {

    public static final UdUserRowMapper INSTANCE = new UdUserRowMapper();

    @Override
    public UserEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserEntity user = new UserEntity();
        user.setId(rs.getObject("id", UUID.class));
        user.setUsername(rs.getString("username"));
        user.setCurrency(rs.getString("currency"));
        user.setFirstName(rs.getString("firstname"));
        user.setSurname(rs.getString("surname"));
        user.setPhoto(rs.getBytes("photo"));
        user.setPhotoSmall(rs.getBytes("photo_small"));
        user.setFullName(rs.getString("full_name"));

        return user;
    }
}
