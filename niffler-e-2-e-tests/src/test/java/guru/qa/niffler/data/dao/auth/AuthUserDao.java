package guru.qa.niffler.data.dao.auth;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;

import java.util.Optional;

public interface AuthUserDao {

    Optional<AuthUserEntity> findByUsername(String username);

    AuthUserEntity create(AuthUserEntity authUser);
}
