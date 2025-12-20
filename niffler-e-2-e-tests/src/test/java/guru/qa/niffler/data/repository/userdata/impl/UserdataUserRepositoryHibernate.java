package guru.qa.niffler.data.repository.userdata.impl;

import guru.qa.niffler.data.entity.userdata.FriendshipEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.userdata.UserdataUserRepository;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.jpa.EntityMangers.em;
import static guru.qa.niffler.helper.TestConstantHolder.CFG;
import static guru.qa.niffler.model.enums.FriendshipStatus.ACCEPTED;
import static guru.qa.niffler.model.enums.FriendshipStatus.PENDING;
import static java.time.LocalDate.now;

public class UserdataUserRepositoryHibernate implements UserdataUserRepository {

    private final EntityManager em = em(CFG.userdataJdbcUrl());

    @Override
    public Optional<UserEntity> findById(UUID id) {

        return Optional.ofNullable(em.find(UserEntity.class, id));
    }

    @Override
    public UserEntity create(UserEntity userEntity) {
        em.joinTransaction();
        em.persist(userEntity);
        return userEntity;
    }

    @Override
    public void addIncomeInvitation(UserEntity requester, UserEntity addressee) {
        FriendshipEntity entity = new FriendshipEntity();
        entity.setRequester(requester);
        entity.setAddressee(addressee);
        entity.setStatus(PENDING);
        entity.setCreatedDate(java.sql.Date.valueOf(now()));

        em.persist(entity);
    }

    @Override
    public void addOutcomeInvitation(UserEntity requester, UserEntity addressee) {
        FriendshipEntity entity = new FriendshipEntity();
        entity.setRequester(addressee);
        entity.setAddressee(requester);
        entity.setStatus(PENDING);
        entity.setCreatedDate(java.sql.Date.valueOf(now()));

        em.persist(entity);
    }

    @Override
    public void addFriend(UserEntity requester, UserEntity addressee) {
        FriendshipEntity friendship1 = new FriendshipEntity();
        friendship1.setRequester(requester);
        friendship1.setAddressee(addressee);
        friendship1.setStatus(ACCEPTED);
        friendship1.setCreatedDate(java.sql.Date.valueOf(now()));

        FriendshipEntity friendship2 = new FriendshipEntity();
        friendship2.setRequester(addressee);
        friendship2.setAddressee(requester);
        friendship2.setStatus(ACCEPTED);
        friendship2.setCreatedDate(java.sql.Date.valueOf(now()));

        em.persist(List.of(friendship1, friendship2));
    }
}
