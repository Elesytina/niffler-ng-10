package guru.qa.niffler.data.dao.userdata.friendship;

import guru.qa.niffler.data.entity.userdata.FriendshipEntity;
import guru.qa.niffler.data.tpl.DataSources;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

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
    public void createAll(List<FriendshipEntity> entities) {
        template.batchUpdate(
                """
                        INSERT INTO friendship
                        (requester_id, addressee_id, status, created_date)
                        values (?,?,?,?)
                        """,
                new BatchPreparedStatementSetter() {

                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setObject(1, entities.get(i).getRequester().getId());
                        ps.setObject(2, entities.get(i).getAddressee().getId());
                        ps.setString(3, entities.get(i).getStatus().name());
                        ps.setDate(4, Date.valueOf(entities.get(i).getCreatedDate().toString()));
                    }

                    @Override
                    public int getBatchSize() {
                        return entities.size();
                    }
                });
    }

    @Override
    public void deleteAll(UUID userId) {
        template.update(conn -> {
            PreparedStatement ps = conn
                    .prepareStatement(
                            """
                                    DELETE FROM friendship
                                    WHERE requester_id= ?
                                    OR addressee_id= ?
                                    """);
            ps.setObject(1, userId);
            ps.setObject(2, userId);

            if (ps.executeUpdate() == 0) {
                throw new RuntimeException("Failed to delete friendship");
            }

            return ps;
        });

    }
}
