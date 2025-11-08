package guru.qa.niffler.service.userdata;

import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.dao.impl.UserdataUserDaoJdbc;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.userdata.UserJson;

import java.util.Optional;
import java.util.UUID;

public class UserDataUserDbClient implements UserDataUserClient {

    private final UserdataUserDao userDao = new UserdataUserDaoJdbc();

    @Override
    public UserJson getUserById(UUID id) {
        Optional<UserEntity> userEntity = userDao.findById(id);

            if (userEntity.isPresent()) {

            return UserJson.fromEntity(userEntity.get());
        }
        throw new RuntimeException("Failed to find user");
    }

    @Override
    public UserJson getUserByUsername(String username) {
        Optional<UserEntity> userEntity = userDao.findByUsername(username);

            if (userEntity.isPresent()) {

            return UserJson.fromEntity(userEntity.get());
        }
        throw new RuntimeException("Failed to find user");
    }

    @Override
    public UserJson createUser(UserJson userJson) {
        UserEntity entity = UserEntity.fromJson(userJson);
        UserEntity userEntity = userDao.create(entity);

        return UserJson.fromEntity(userEntity);
    }

    @Override
    public void deleteUser(UserJson userJson) {
        UserEntity entity = UserEntity.fromJson(userJson);
        boolean isSuccess = userDao.delete(entity);

        if (!isSuccess) {
            throw new RuntimeException("Failed to delete user");
        }
    }
}
