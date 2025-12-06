package guru.qa.niffler.data.dao.userdata;

import java.util.Optional;
import java.util.UUID;

public interface UserdataUserDao {

    Optional<UserEntity> findById(UUID id);

    Optional<UserEntity> findByUsername(String username);

    UserEntity create(UserEntity entity);

    boolean delete(UserEntity spendEntity);

}
