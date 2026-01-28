package guru.qa.niffler.data.repository.userdata.impl;

import guru.qa.niffler.data.dao.userdata.UserdataUserDao;
import guru.qa.niffler.data.dao.userdata.UserdataUserDaoSpringJdbc;
import guru.qa.niffler.data.dao.userdata.friendship.FriendshipDao;
import guru.qa.niffler.data.dao.userdata.friendship.FriendshipDaoSpringJdbc;
import guru.qa.niffler.data.entity.userdata.FriendshipEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.userdata.UserdataUserRepository;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.model.enums.FriendshipStatus.ACCEPTED;
import static guru.qa.niffler.model.enums.FriendshipStatus.PENDING;
import static java.time.LocalDate.now;

@ParametersAreNonnullByDefault
public class UserdataUserRepositorySpringImpl implements UserdataUserRepository {

    private final FriendshipDao friendshipDao = new FriendshipDaoSpringJdbc();
    private final UserdataUserDao userdataUserDao = new UserdataUserDaoSpringJdbc();

    @Override
    public Optional<UserEntity> findById(UUID id) {

        return userdataUserDao.findById(id);
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {

        return userdataUserDao.findByUsername(username);
    }

    @Override
    public UserEntity create(UserEntity entity) {

        return userdataUserDao.create(entity);
    }

    @Override
    public UserEntity update(UserEntity user) {

        return userdataUserDao.update(user);
    }

    @Override
    public void sendInvitation(UserEntity requester, UserEntity addressee) {
        addInvitation(addressee, requester);
    }

    @Override
    public void addFriend(UserEntity requester, UserEntity addressee) {
        FriendshipEntity entity1 = new FriendshipEntity();
        entity1.setRequester(requester);
        entity1.setAddressee(addressee);
        entity1.setStatus(ACCEPTED);
        entity1.setCreatedDate(java.sql.Date.valueOf(now()));

        FriendshipEntity entity2 = new FriendshipEntity();
        entity2.setRequester(addressee);
        entity2.setAddressee(requester);
        entity2.setStatus(ACCEPTED);
        entity2.setCreatedDate(java.sql.Date.valueOf(now()));

        friendshipDao.createAll(List.of(entity1, entity2));
    }

    @Override
    public void remove(UserEntity user) {
        friendshipDao.deleteAll(user.getId());
        userdataUserDao.delete(user);
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
