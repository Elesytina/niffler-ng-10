package guru.qa.niffler.data.mapper;

import guru.qa.niffler.data.entity.userdata.FriendshipEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.enums.FriendshipStatus;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FriendshipRowMapper implements RowMapper<FriendshipEntity> {

    public static final FriendshipRowMapper INSTANCE = new FriendshipRowMapper();

    @Override
    public FriendshipEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        FriendshipEntity friendship = new FriendshipEntity();
        UserEntity requester = new UserEntity();
        requester.setId(rs.getObject("requester_id", UUID.class));
        friendship.setRequester(requester);
        UserEntity addressee = new UserEntity();
        addressee.setId(rs.getObject("addressee_id", UUID.class));
        friendship.setAddressee(addressee);
        friendship.setStatus(FriendshipStatus.valueOf(rs.getString("status")));
        friendship.setCreatedDate(rs.getDate("created_date"));

        return friendship;
    }
}
