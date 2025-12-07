package guru.qa.niffler.data.mapper.extractor;

import guru.qa.niffler.data.entity.userdata.UserEntity;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserdataUserResultSetExtractor implements ResultSetExtractor<UserEntity> {
    @Override
    public UserEntity extractData(ResultSet rs) throws SQLException, DataAccessException {
        return null;
    }
}
