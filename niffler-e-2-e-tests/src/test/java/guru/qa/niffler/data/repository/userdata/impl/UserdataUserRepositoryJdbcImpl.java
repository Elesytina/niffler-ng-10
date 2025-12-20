package guru.qa.niffler.data.repository.userdata.impl;

import guru.qa.niffler.data.dao.userdata.UserdataUserDao;
import guru.qa.niffler.data.dao.userdata.UserdataUserDaoJdbc;
import guru.qa.niffler.data.dao.userdata.friendship.FriendshipDao;
import guru.qa.niffler.data.dao.userdata.friendship.FriendshipDaoJdbc;
import guru.qa.niffler.data.entity.userdata.FriendshipEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.userdata.UserdataUserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.model.enums.FriendshipStatus.ACCEPTED;
import static guru.qa.niffler.model.enums.FriendshipStatus.PENDING;
import static java.time.LocalDate.now;

public class UserdataUserRepositoryJdbcImpl implements UserdataUserRepository {
    private final FriendshipDao friendshipDao = new FriendshipDaoJdbc();
    private final UserdataUserDao userdataUserDao = new UserdataUserDaoJdbc();

    @Override
    public Optional<UserEntity> findById(UUID id) {

        return userdataUserDao.findById(id);
    }

    @Override
    public UserEntity create(UserEntity entity) {

        return userdataUserDao.create(entity);
    }

    @Override
    public void addIncomeInvitation(UserEntity requester, UserEntity addressee) {
        addInvitation(addressee, requester);
    }

    @Override
    public void addOutcomeInvitation(UserEntity requester, UserEntity addressee) {
        addInvitation(requester, addressee);
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

        friendshipDao.createAll(List.of(friendship1, friendship2));
    }

    private void addInvitation(UserEntity requester, UserEntity addressee) {
        FriendshipEntity entity = new FriendshipEntity();
        entity.setRequester(requester);
        entity.setAddressee(addressee);
        entity.setStatus(PENDING);
        entity.setCreatedDate(java.sql.Date.valueOf(now()));

        friendshipDao.create(entity);
    }
}
