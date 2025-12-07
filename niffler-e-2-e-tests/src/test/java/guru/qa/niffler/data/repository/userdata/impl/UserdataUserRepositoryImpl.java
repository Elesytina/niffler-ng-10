package guru.qa.niffler.data.repository.userdata.impl;

import guru.qa.niffler.data.dao.userdata.UserdataUserDao;
import guru.qa.niffler.data.dao.userdata.UserdataUserDaoSpringJdbc;
import guru.qa.niffler.data.dao.userdata.friendship.FriendshipDao;
import guru.qa.niffler.data.dao.userdata.friendship.FriendshipDaoSpringJdbc;
import guru.qa.niffler.data.entity.userdata.FriendshipEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.userdata.UserdataUserRepository;

import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.model.enums.FriendshipStatus.ACCEPTED;
import static guru.qa.niffler.model.enums.FriendshipStatus.PENDING;
import static java.time.LocalDate.now;

public class UserdataUserRepositoryImpl implements UserdataUserRepository {
    private final FriendshipDao friendshipDao = new FriendshipDaoSpringJdbc();
    private final UserdataUserDao userdataUserDao = new UserdataUserDaoSpringJdbc();

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
        FriendshipEntity entity = new FriendshipEntity();
        entity.setRequester(requester);
        entity.setAddressee(addressee);
        entity.setStatus(ACCEPTED);
        entity.setCreatedDate(java.sql.Date.valueOf(now()));

        friendshipDao.update(entity);
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
