package guru.qa.niffler.data.mapper.extractor;

import guru.qa.niffler.data.entity.userdata.UserEntity;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;

public class UserdataUserResultSetExtractor implements ResultSetExtractor<UserEntity> {
    @Override
    public UserEntity extractData(ResultSet rs) throws DataAccessException {
        return null;
    }
}
