package guru.qa.niffler.service.user;

import guru.qa.niffler.data.dao.auth.impl.AuthUserSpringDaoJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.auth.AuthUserRepository;
import guru.qa.niffler.data.repository.auth.impl.AuthUserRepositorySpringJdbcImpl;
import guru.qa.niffler.data.repository.userdata.UserdataUserRepository;
import guru.qa.niffler.data.repository.userdata.impl.UserdataUserRepositorySpringJdbcImpl;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.auth.AuthUserJson;
import guru.qa.niffler.model.enums.Authority;
import guru.qa.niffler.model.userdata.UserJson;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.helper.TestConstantHolder.CFG;

@Slf4j
public class UserDbClient {
    private final AuthUserRepository authUserRepository = new AuthUserRepositorySpringJdbcImpl();
    private final UserdataUserRepository userdataUserRepository = new UserdataUserRepositorySpringJdbcImpl();
    private final AuthUserSpringDaoJdbc authUserDaoSpring = new AuthUserSpringDaoJdbc();

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userdataJdbcUrl());

    public UserJson createUserSpringJdbc(UserJson userJson, String password) {
        return xaTransactionTemplate.execute(() -> {
            authUserRepository.create(getAuthUserEntity(userJson, password));

            var user = userdataUserRepository.create(UserEntity.fromJson(userJson));

            return UserJson.fromEntity(user);
        });
    }

    public UserJson getUserById(UUID id) {
        Optional<UserEntity> userEntity = userdataUserRepository.findById(id);

        if (userEntity.isPresent()) {

            return UserJson.fromEntity(userEntity.get());
        }
        throw new RuntimeException("Failed to find user by id: %s".formatted(id));
    }

    public void addFriend(UserJson user, UserJson friend) {
        userdataUserRepository.addFriend(UserEntity.fromJson(user), UserEntity.fromJson(friend));
    }

    public AuthUserJson getAuthUserByName(String name) {
        Optional<AuthUserEntity> authEntity = authUserDaoSpring.findByUsername(name);

        if (authEntity.isPresent()) {

            return AuthUserJson.fromEntity(authEntity.get());
        }
        throw new RuntimeException("Failed to find user");
    }

    public static AuthUserEntity getAuthUserEntity(UserJson userJson, String password) {
        AuthUserEntity entity = new AuthUserEntity();
        entity.setUsername(userJson.username());
        entity.setPassword(password);
        entity.setEnabled(true);
        entity.setAccountNonExpired(true);
        entity.setAccountNonLocked(true);
        entity.setCredentialsNonExpired(true);

        return entity;
    }

    public static AuthorityEntity getAuthorityEntity(AuthUserEntity savedEntity, Authority authority) {
        AuthorityEntity authorityEntity = new AuthorityEntity();
        authorityEntity.setAuthority(authority);
        authorityEntity.setUser(savedEntity);

        return authorityEntity;
    }

}
