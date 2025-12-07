package guru.qa.niffler.data.repository.userdata.impl;

import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.userdata.UserdataUserRepository;

import java.util.Optional;
import java.util.UUID;

public class UserdataUserRepositoryImpl implements UserdataUserRepository {

    @Override
    public Optional<UserEntity> findById(UUID id) {
        return Optional.empty();
    }

    @Override
    public UserEntity create(UserEntity authUser) {
        return null;
    }

    @Override
    public void addIncomeInvitation(UserEntity requester, UserEntity target) {

    }

    @Override
    public void addOutcomeInvitation(UserEntity requester, UserEntity target) {

    }

    @Override
    public void addFriend(UserEntity requester, UserEntity target) {

    }
}
