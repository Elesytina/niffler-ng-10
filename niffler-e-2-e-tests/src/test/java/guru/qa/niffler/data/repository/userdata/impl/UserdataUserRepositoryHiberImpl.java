package guru.qa.niffler.data.repository.userdata.impl;

import guru.qa.niffler.data.entity.userdata.FriendshipEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.jpa.EntityManagers;
import guru.qa.niffler.data.repository.userdata.UserdataUserRepository;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.helper.TestConstantHolder.CFG;
import static guru.qa.niffler.model.enums.FriendshipStatus.ACCEPTED;
import static guru.qa.niffler.model.enums.FriendshipStatus.PENDING;

public class UserdataUserRepositoryHiberImpl implements UserdataUserRepository {

    private final EntityManager em = EntityManagers.em(CFG.userdataJdbcUrl());

    @Override
    public UserEntity create(UserEntity userEntity) {
        em.joinTransaction();
        em.persist(userEntity);

        return userEntity;
    }

    @Override
    public UserEntity update(UserEntity user) {
        em.joinTransaction();
        em.merge(user);

        return user;
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {

        return Optional.ofNullable(em.find(UserEntity.class, id));
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        var sql = """
                FROM UserEntity user
                WHERE user.username=:username
                """;

        return Optional.ofNullable(
                em.createQuery(sql, UserEntity.class)
                        .setParameter("username", username)
                        .getSingleResultOrNull());
    }

    @Override
    public void sendInvitation(UserEntity requester, UserEntity addressee) {
        em.joinTransaction();
        addressee.addFriends(PENDING, requester);
    }

    @Override
    public void addFriend(UserEntity requester, UserEntity addressee) {
        em.joinTransaction();
        requester.addFriends(ACCEPTED, addressee);
        addressee.addFriends(ACCEPTED, requester);
    }

    @Override
    public void remove(UserEntity user) {
        em.joinTransaction();
        List<FriendshipEntity> friendshipEntities = user.getFriendshipAddressees();
        user.removeFriends(friendshipEntities.stream().map(FriendshipEntity::getAddressee).toArray(UserEntity[]::new));
        List<FriendshipEntity> requestEntities = user.getFriendshipRequests();
        user.removeInvites(requestEntities.stream().map(FriendshipEntity::getAddressee).toArray(UserEntity[]::new));
        em.remove(user);
    }
}
