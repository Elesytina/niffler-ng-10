package guru.qa.niffler.repository;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;

import java.util.Optional;
import java.util.UUID;

public interface AuthUserRepository {
    Optional<AuthUserEntity> findById(UUID id);

    AuthUserEntity create(AuthUserEntity authUser);
}
