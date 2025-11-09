package guru.qa.niffler.service.userdata;

import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.userdata.UserdataUserDaoJdbc;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.userdata.UserJson;

import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.helper.TestConstantHolder.CFG;

public class UserDataUserDbClient implements UserDataUserClient {

    @Override
    public UserJson getUserById(UUID id) {
        return Databases.transaction(new Databases.XaFunction<>(connect -> {
            Optional<UserEntity> userEntity = new UserdataUserDaoJdbc(connect).findById(id);

            if (userEntity.isPresent()) {

                return UserJson.fromEntity(userEntity.get());
            }
            throw new RuntimeException("Failed to find user");
        }, CFG.userdataJdbcUrl(), 2));
    }

    @Override
    public UserJson getUserByUsername(String username) {
        return Databases.transaction(new Databases.XaFunction<>(connect -> {
            Optional<UserEntity> userEntity = new UserdataUserDaoJdbc(connect).findByUsername(username);

            if (userEntity.isPresent()) {

                return UserJson.fromEntity(userEntity.get());
            }
            throw new RuntimeException("Failed to find user");
        }, CFG.userdataJdbcUrl(), 2));
    }

    @Override
    public UserJson createUser(UserJson userJson) {
        return Databases.transaction(new Databases.XaFunction<>(connect -> {
            UserEntity entity = UserEntity.fromJson(userJson);
            UserEntity userEntity = new UserdataUserDaoJdbc(connect).create(entity);

            return UserJson.fromEntity(userEntity);
        }, CFG.userdataJdbcUrl(), 2));
    }

    @Override
    public void deleteUser(UserJson userJson) {
        Databases.transaction(new Databases.XaConsumer(connect -> {
            UserEntity entity = UserEntity.fromJson(userJson);
            boolean isSuccess = new UserdataUserDaoJdbc(connect).delete(entity);

            if (!isSuccess) {
                throw new RuntimeException("Failed to delete user");
            }
        }, CFG.userdataJdbcUrl(), 2));
    }
}
