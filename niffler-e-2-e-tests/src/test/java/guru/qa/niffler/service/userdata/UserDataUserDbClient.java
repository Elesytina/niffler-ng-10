package guru.qa.niffler.service.userdata;

import guru.qa.niffler.data.dao.userdata.UserdataUserDao;
import guru.qa.niffler.data.dao.userdata.UserdataUserDaoJdbc;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.model.userdata.UserJson;

import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.helper.TestConstantHolder.CFG;

public class UserDataUserDbClient implements UserDataUserClient {
    private final UserdataUserDao userdataUserDao = new UserdataUserDaoJdbc();
    private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(CFG.authJdbcUrl());

    @Override
    public UserJson getUserById(UUID id) {
        return jdbcTxTemplate.execute(() -> {
            Optional<UserEntity> userEntity = userdataUserDao.findById(id);

            if (userEntity.isPresent()) {

                return UserJson.fromEntity(userEntity.get());
            }
            throw new RuntimeException("Failed to find user");
        });
    }

    @Override
    public UserJson getUserByUsername(String username) {
        return jdbcTxTemplate.execute(() -> {
            Optional<UserEntity> userEntity = userdataUserDao.findByUsername(username);

            if (userEntity.isPresent()) {

                return UserJson.fromEntity(userEntity.get());
            }
            throw new RuntimeException("Failed to find user");
        });
    }

    @Override
    public UserJson createUser(UserJson userJson) {
        return jdbcTxTemplate.execute(() -> {
            UserEntity entity = UserEntity.fromJson(userJson);
            UserEntity userEntity = userdataUserDao.create(entity);

            return UserJson.fromEntity(userEntity);
        });
    }

    @Override
    public void deleteUser(UserJson userJson) {
        jdbcTxTemplate.execute(() -> {
            UserEntity entity = UserEntity.fromJson(userJson);
            boolean isSuccess = userdataUserDao.delete(entity);

            if (!isSuccess) {
                throw new RuntimeException("Failed to delete user");
            }
            return null;
        });
    }
}
