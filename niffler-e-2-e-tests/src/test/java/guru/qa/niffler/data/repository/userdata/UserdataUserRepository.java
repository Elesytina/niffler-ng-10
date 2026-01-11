package guru.qa.niffler.data.repository.userdata;

import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.userdata.impl.UserdataUserRepositoryHiberImpl;
import guru.qa.niffler.data.repository.userdata.impl.UserdataUserRepositoryJdbcImpl;
import guru.qa.niffler.data.repository.userdata.impl.UserdataUserRepositorySpringImpl;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public interface UserdataUserRepository {

    @Nonnull
    static UserdataUserRepository getInstance() {
        return switch (System.getProperty("repository", "jpa")) {
            case "jpa" -> new UserdataUserRepositoryHiberImpl();
            case "jdbc" -> new UserdataUserRepositoryJdbcImpl();
            case "sjdbc" -> new UserdataUserRepositorySpringImpl();
            default -> throw new IllegalStateException("Unsupported repository: " + System.getProperty("repository"));
        };
    }

    UserEntity create(UserEntity user);

    UserEntity update(UserEntity user);

    Optional<UserEntity> findById(UUID id);

    Optional<UserEntity> findByUsername(String username);

    void sendInvitation(UserEntity requester, UserEntity addressee);

    void addFriend(UserEntity requester, UserEntity addressee);

    void remove(UserEntity user);
}
