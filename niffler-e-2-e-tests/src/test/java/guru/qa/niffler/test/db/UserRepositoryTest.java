package guru.qa.niffler.test.db;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.auth.AuthUserRepository;
import guru.qa.niffler.data.repository.auth.impl.AuthUserRepositorySpringJdbcImpl;
import guru.qa.niffler.data.repository.userdata.UserdataUserRepository;
import guru.qa.niffler.data.repository.userdata.impl.UserdataUserRepositoryImpl;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.auth.AuthUserJson;
import guru.qa.niffler.model.userdata.UserJson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.helper.TestConstantHolder.CFG;
import static guru.qa.niffler.utils.RandomDataUtils.*;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;

public class UserRepositoryTest {
    UserdataUserRepository userRepository = new UserdataUserRepositoryImpl();
    AuthUserRepository authUserRepository = new AuthUserRepositorySpringJdbcImpl();

    @Test
    void shouldRegisterNewUserWithSpringJdbc() {
        var xaTxTpl = new XaTransactionTemplate(CFG.userdataJdbcUrl(), CFG.authJdbcUrl());
        var username = "Ivan_Vasilievitch-%s".formatted(randomNumeric(2));

        AuthUserJson authUserJson = new AuthUserJson(
                null,
                username,
                "123",
                true, true, true, true);

        UserJson userJson = new UserJson(null,
                username,
                randomCurrency(),
                randomName(),
                randomSurname(),
                randomFullName(),
                null,
                null);

        UserEntity userEntity = xaTxTpl.execute(() -> {
            authUserRepository.create(AuthUserEntity.fromJson(authUserJson));

            return userRepository.create(UserEntity.fromJson(userJson));
        });

        Assertions.assertNotNull(userEntity.getId(), "User should have been created");
    }


    @Test
    void shouldAddFriend() {
        Optional<UserEntity> requesterEntity = userRepository.findById(UUID.fromString("bd755702-c246-11f0-9783-ceceb74d3cd5"));
        Optional<UserEntity> addresseeEntity = userRepository.findById(UUID.fromString("b80e92b2-c000-11f0-b43d-d62d6fb87ff1"));

        Assertions.assertTrue(requesterEntity.isPresent(), "User should been found");
        Assertions.assertTrue(addresseeEntity.isPresent(), "User should been found");

        userRepository.addOutcomeInvitation(requesterEntity.get(), addresseeEntity.get());
        userRepository.addIncomeInvitation(requesterEntity.get(), addresseeEntity.get());

        userRepository.addFriend(requesterEntity.get(), addresseeEntity.get());
    }

}
