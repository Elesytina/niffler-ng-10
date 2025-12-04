package guru.qa.niffler.data.repository.auth;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;

import java.util.Optional;

public interface AuthUserRepository {
    Optional<AuthUserEntity> findByUsername(String username);

    AuthUserEntity create(AuthUserEntity authUser);
}
