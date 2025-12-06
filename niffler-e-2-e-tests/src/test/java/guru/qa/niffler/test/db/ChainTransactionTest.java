package guru.qa.niffler.test.db;

import guru.qa.niffler.data.dao.auth.AuthAuthorityDao;
import guru.qa.niffler.data.dao.auth.impl.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.dao.auth.impl.AuthAuthoritySpringDaoJdbc;
import guru.qa.niffler.data.dao.auth.impl.AuthUserDaoJdbc;
import guru.qa.niffler.data.dao.auth.impl.AuthUserSpringDaoJdbc;
import guru.qa.niffler.data.dao.userdata.UserdataUserDao;
import guru.qa.niffler.data.dao.userdata.UserdataUserDaoJdbc;
import guru.qa.niffler.data.dao.userdata.UserdataUserDaoSpringJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.tpl.DataSources;
import guru.qa.niffler.model.userdata.UserJson;
import guru.qa.niffler.service.user.UserDbClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.stream.Stream;

import static guru.qa.niffler.helper.TestConstantHolder.CFG;
import static guru.qa.niffler.model.enums.Authority.read;
import static guru.qa.niffler.model.enums.Authority.write;
import static guru.qa.niffler.service.user.UserDbClient.getAuthUserEntity;
import static guru.qa.niffler.service.user.UserDbClient.getAuthorityEntity;
import static guru.qa.niffler.utils.RandomDataUtils.*;

public class ChainTransactionTest {
    private final AuthUserSpringDaoJdbc authUserDaoSpring = new AuthUserSpringDaoJdbc();
    private final AuthAuthorityDao authorityDaoSpring = new AuthAuthoritySpringDaoJdbc();
    private final UserdataUserDao udUserDaoSpring = new UserdataUserDaoSpringJdbc();
    private final UserDbClient userDbClient = new UserDbClient();
    private final TransactionTemplate chainTransactionTemplate = new TransactionTemplate(
            new ChainedTransactionManager(
                    new JdbcTransactionManager(DataSources.getDataSource(CFG.authJdbcUrl())),
                    new JdbcTransactionManager(DataSources.getDataSource(CFG.userdataJdbcUrl()))
            ));

    // а здесь сработало
    @Test
    void shouldNotCreateNewAuthUserWithChainedTransactionManager() {
        var username = "Nikolai-22";
        UserJson userJson = new UserJson(null,
                username,
                randomCurrency(),
                randomName(),
                randomSurname(),
                randomFullName(),
                null,
                null);
        try {
            chainTransactionTemplate.execute(status -> {
                AuthUserEntity authUserEntity = authUserDaoSpring.create(getAuthUserEntity(userJson, "123"));

                authorityDaoSpring.create(
                        Stream.of(read, write)
                                .map(authority -> getAuthorityEntity(authUserEntity, authority))
                                .toList());

                var userEntity = UserEntity.fromJson(userJson);
                userEntity.setUsername(null);
                udUserDaoSpring.create(userEntity);

                return authUserEntity;
            });
        } catch (Exception ignored) {
        }
        Assertions.assertThrows(RuntimeException.class, () -> userDbClient.getAuthUserByName(username));
    }

    //c jdbc не работает
    @Test
    void shouldNotCreateNewAuthUserWithChainedTransactionManagerForJdbc() {
        var username = "Nikolai-28";
        UserJson userJson = new UserJson(null,
                username,
                randomCurrency(),
                randomName(),
                randomSurname(),
                randomFullName(),
                null,
                null);
        try {
            chainTransactionTemplate.execute(status -> {
                AuthUserEntity authUserEntity = new AuthUserDaoJdbc().create(getAuthUserEntity(userJson, "1234"));

                new AuthAuthorityDaoJdbc().create(
                        Stream.of(read, write)
                                .map(authority -> getAuthorityEntity(authUserEntity, authority))
                                .toList());

                var userEntity = UserEntity.fromJson(userJson);
                userEntity.setUsername(null);
                new UserdataUserDaoJdbc().create(userEntity);

                return authUserEntity;
            });
        } catch (Exception ignored) {
        }
        Assertions.assertThrows(RuntimeException.class, () -> userDbClient.getAuthUserByName(username));
    }

}
