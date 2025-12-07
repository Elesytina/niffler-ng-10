package guru.qa.niffler.data.dao.userdata.friendship;

import guru.qa.niffler.data.entity.userdata.FriendshipEntity;
import guru.qa.niffler.data.tpl.DataSources;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Date;
import java.sql.PreparedStatement;

import static guru.qa.niffler.helper.TestConstantHolder.CFG;

public class FriendshipDaoSpringJdbc implements FriendshipDao {

    private final JdbcTemplate template = new JdbcTemplate(DataSources.getDataSource(CFG.userdataJdbcUrl()));

    @Override
    public FriendshipEntity create(FriendshipEntity entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(
                    """
                            INSERT INTO friendship
                            (requester_id, addressee_id, status, created_date)
                            values (?,?,?,?)
                            """);
            ps.setObject(1, entity.getRequester().getId());
            ps.setObject(2, entity.getAddressee().getId());
            ps.setString(3, entity.getStatus().name());
            ps.setDate(4, Date.valueOf(entity.getCreatedDate().toString()));

            return ps;
        }, keyHolder);

        return entity;
    }

    @Override
    public FriendshipEntity update(FriendshipEntity entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(
                    """
                            UPDATE friendship
                            SET status = ?
                            WHERE requester_id in (?,?)
                            """);
            ps.setString(1, entity.getStatus().name());
            ps.setObject(2, entity.getRequester().getId());
            ps.setObject(3, entity.getAddressee().getId());

            return ps;
        }, keyHolder);

        return entity;
    }
}
