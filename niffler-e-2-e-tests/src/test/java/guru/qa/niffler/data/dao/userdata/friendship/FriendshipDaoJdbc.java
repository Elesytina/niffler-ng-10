package guru.qa.niffler.data.dao.userdata.friendship;

import guru.qa.niffler.data.entity.userdata.FriendshipEntity;
import guru.qa.niffler.data.tpl.JdbcConnectionHolder;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static guru.qa.niffler.data.tpl.Connections.getHolder;
import static guru.qa.niffler.helper.TestConstantHolder.CFG;

public class FriendshipDaoJdbc implements FriendshipDao {

    private final JdbcConnectionHolder connectionHolder = getHolder(CFG.userdataJdbcUrl());

    @Override
    public FriendshipEntity create(FriendshipEntity entity) {
        try (PreparedStatement ps = connectionHolder.getConnection()
                .prepareStatement(
                        """
                                INSERT INTO friendship
                                (requester_id, addressee_id, status, created_date)
                                values (?,?,?,?)
                                """)) {
            ps.setObject(1, entity.getRequester().getId());
            ps.setObject(2, entity.getAddressee().getId());
            ps.setString(3, entity.getStatus().name());
            ps.setDate(4, Date.valueOf(entity.getCreatedDate().toString()));

            if (ps.executeUpdate() != 0) {

                return entity;
            }
            throw new RuntimeException("Failed to insert new user");
        } catch (SQLException exception) {
            throw new RuntimeException("Could not insert a new friendship", exception);
        }
    }

    @Override
    public FriendshipEntity update(FriendshipEntity entity) {
        try (PreparedStatement ps = connectionHolder.getConnection()
                .prepareStatement(
                        """
                                UPDATE friendship
                                SET status = ?
                                WHERE requester_id in (?,?)
                                """)) {
            ps.setString(1, entity.getStatus().name());
            ps.setObject(2, entity.getRequester().getId());
            ps.setObject(3, entity.getAddressee().getId());

            if (ps.executeUpdate() != 0) {

                return entity;
            }
            throw new RuntimeException("Failed to update the friendship");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update the friendship", e);
        }
    }
}
