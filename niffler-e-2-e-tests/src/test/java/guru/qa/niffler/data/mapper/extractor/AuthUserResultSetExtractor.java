package guru.qa.niffler.data.mapper.extractor;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.model.enums.Authority;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AuthUserResultSetExtractor implements ResultSetExtractor<AuthUserEntity> {

    public static final AuthUserResultSetExtractor INSTANCE = new AuthUserResultSetExtractor();

    @Override
    public AuthUserEntity extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<UUID, AuthUserEntity> userMap = new ConcurrentHashMap<>();
        List<AuthorityEntity> authorities = new ArrayList<>();
        UUID id = null;

        while (rs.next()) {
            id = rs.getObject("id", UUID.class);
            userMap.computeIfAbsent(id,
                    userId -> {
                        try {
                            var user = new AuthUserEntity();
                            user.setId(userId);
                            user.setUsername(rs.getString("username"));
                            user.setPassword(rs.getString("password"));
                            user.setEnabled(rs.getBoolean("enabled"));
                            user.setAccountNonExpired(rs.getBoolean("account_non_expired"));
                            user.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
                            user.setAccountNonLocked(rs.getBoolean("account_non_locked"));
                            user.setAuthorities(authorities.stream()
                                    .map(authorityEntity -> {
                                        try {
                                            authorityEntity.setUser(user);
                                            authorityEntity.setId(rs.getObject("authority_id", UUID.class));
                                            authorityEntity.setAuthority(Authority.valueOf(rs.getString("authority")));
                                            return authorityEntity;
                                        } catch (SQLException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }).toList());
                            return user;
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    });

            AuthorityEntity authority = new AuthorityEntity();
            authority.setId(rs.getObject("a.id", UUID.class));
            authority.setUser(userMap.get(id));
            authority.setAuthority(Authority.valueOf(rs.getString("a.authority")));
            authorities.add(authority);
        }
        AuthUserEntity user = userMap.get(id);
        user.setAuthorities(authorities);

        return user;
    }
}
