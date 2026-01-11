package guru.qa.niffler.data.repository.auth;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.repository.auth.impl.AuthUserRepositoryHiberImpl;
import guru.qa.niffler.data.repository.auth.impl.AuthUserRepositoryJdbcImpl;
import guru.qa.niffler.data.repository.auth.impl.AuthUserRepositorySpringJdbcImpl;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public interface AuthUserRepository {

    @Nonnull
    static AuthUserRepository getInstance() {
        return switch (System.getProperty("repository", "jpa")) {
            case "jpa" -> new AuthUserRepositoryHiberImpl();
            case "jdbc" -> new AuthUserRepositoryJdbcImpl();
            case "sjdbc" -> new AuthUserRepositorySpringJdbcImpl();
            default -> throw new IllegalStateException("Unsupported repository: " + System.getProperty("repository"));
        };
    }

    AuthUserEntity create(AuthUserEntity authUser);

    AuthUserEntity update(AuthUserEntity authUser);

    Optional<AuthUserEntity> findById(UUID id);

    Optional<AuthUserEntity> findByUsername(String name);

    void remove(UUID id);
}
