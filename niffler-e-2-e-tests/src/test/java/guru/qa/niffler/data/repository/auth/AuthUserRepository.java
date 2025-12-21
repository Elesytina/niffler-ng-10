package guru.qa.niffler.data.repository.auth;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;

import java.util.Optional;
import java.util.UUID;

public interface AuthUserRepository {

    AuthUserEntity create(AuthUserEntity authUser);

    AuthUserEntity update(AuthUserEntity authUser);

    Optional<AuthUserEntity> findById(UUID id);

    Optional<AuthUserEntity> findByUsername(String name);

    void remove(UUID id);
}
