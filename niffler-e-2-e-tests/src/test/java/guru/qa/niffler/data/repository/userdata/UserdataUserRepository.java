package guru.qa.niffler.data.repository.userdata;

import guru.qa.niffler.data.entity.userdata.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserdataUserRepository {

    Optional<UserEntity> findById(UUID id);

    UserEntity create(UserEntity authUser);

    void addIncomeInvitation(UserEntity requester, UserEntity target);

    void addOutcomeInvitation(UserEntity requester, UserEntity target);

    void addFriend(UserEntity requester, UserEntity target);
}
