package guru.qa.niffler.data.mapper;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.model.enums.Authority;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AuthUserResultSetExtractor implements ResultSetExtractor<AuthUserEntity> {
    public static final AuthUserResultSetExtractor INSTANCE = new AuthUserResultSetExtractor();

    @Override
    public AuthUserEntity extractData(ResultSet rs) throws SQLException, DataAccessException {
        AuthUserEntity user = new AuthUserEntity();
        List<AuthorityEntity> authorities = new ArrayList<>();

        while (rs.next()) {
            if (user.getId() == null) {
                user.setId(rs.getObject("u.id", UUID.class));
                user.setUsername(rs.getString("u.username"));
                user.setPassword(rs.getString("u.password"));
                user.setEnabled(rs.getBoolean("u.enabled"));
                user.setAccountNonExpired(rs.getBoolean("u.account_non_expired"));
                user.setCredentialsNonExpired(rs.getBoolean("u.credentials_non_expired"));
                user.setAccountNonLocked(rs.getBoolean("u.account_non_locked"));
            }
            AuthorityEntity authority = new AuthorityEntity();
            authority.setId(rs.getObject("a.id", UUID.class));
            authority.setUser(user);
            authority.setAuthority(Authority.valueOf(rs.getString("authority")));
            authorities.add(authority);
        }
        user.setAuthorities(authorities);

        return user;
    }
}
