package guru.qa.niffler.repository.impl;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.repository.AuthUserRepository;

import java.util.Optional;
import java.util.UUID;

public class AuthUserRepositoryImpl implements AuthUserRepository {
    @Override
    public Optional<AuthUserEntity> findById(UUID id) {
        return Optional.empty();
    }

    @Override
    public AuthUserEntity create(AuthUserEntity authUser) {
        return null;
    }
}
