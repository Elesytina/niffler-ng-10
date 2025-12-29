package guru.qa.niffler.service.user;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.auth.AuthUserRepository;
import guru.qa.niffler.data.repository.auth.impl.AuthUserRepositorySpringJdbcImpl;
import guru.qa.niffler.data.repository.userdata.UserdataUserRepository;
import guru.qa.niffler.data.repository.userdata.impl.UserdataUserRepositorySpringImpl;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.auth.AuthUserJson;
import guru.qa.niffler.model.enums.Authority;
import guru.qa.niffler.model.userdata.UserJson;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.helper.TestConstantHolder.CFG;
import static guru.qa.niffler.utils.RandomDataUtils.*;

@Slf4j
public class UserDbClient {

    private final AuthUserRepository authUserRepository = new AuthUserRepositorySpringJdbcImpl();
    private final UserdataUserRepository userdataUserRepository = new UserdataUserRepositorySpringImpl();

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userdataJdbcUrl());

    public UserJson create(String username, String password) {
        return xaTransactionTemplate.execute(() -> {
            UserJson userJson = new UserJson(null,
                    username,
                    randomCurrency(),
                    randomName(),
                    randomSurname(),
                    randomFullName(),
                    null,
                    null);

            authUserRepository.create(getAuthUserEntity(userJson, password));

            var user = userdataUserRepository.create(UserEntity.fromJson(userJson));

            return UserJson.fromEntity(user);
        });
    }

    public UserJson update(UserJson userJson) {
        var user = userdataUserRepository.update(UserEntity.fromJson(userJson));

        return UserJson.fromEntity(user);
    }

    public void delete(UserJson userJson) {
        userdataUserRepository.remove(UserEntity.fromJson(userJson));
    }

    public UserJson findById(UUID id) {
        Optional<UserEntity> userEntity = userdataUserRepository.findById(id);

        if (userEntity.isPresent()) {

            return UserJson.fromEntity(userEntity.get());
        }
        throw new RuntimeException("Failed to find user by id: %s".formatted(id));
    }

    public UserJson findByUsername(String username) {
        Optional<UserEntity> userEntity = userdataUserRepository.findByUsername(username);

        if (userEntity.isPresent()) {

            return UserJson.fromEntity(userEntity.get());
        }
        throw new RuntimeException("Failed to find user by username: %s".formatted(username));
    }

    public void addFriends(UserJson user, int count) {
        xaTransactionTemplate.execute(() -> {
            for (int i = 0; i < count; i++) {
                var username = randomUsername();
                UserEntity friendUserEntity = getDefaultUserEntity(username);
                userdataUserRepository.create(friendUserEntity);
                authUserRepository.create(getDefaultAuthUserEntity(username));

                userdataUserRepository.addFriend(UserEntity.fromJson(user), friendUserEntity);
            }

            return null;
        });
    }

    public void addIncomeInvitations(UserJson user, int count) {
        xaTransactionTemplate.execute(() -> {
            for (int i = 0; i < count; i++) {
                var username = randomUsername();
                UserEntity friendUserEntity = getDefaultUserEntity(username);
                userdataUserRepository.create(friendUserEntity);
                authUserRepository.create(getDefaultAuthUserEntity(username));

                userdataUserRepository.sendInvitation(UserEntity.fromJson(user), friendUserEntity);
            }

            return null;
        });
    }

    public void addOutcomeInvitations(UserJson user, int count) {
        xaTransactionTemplate.execute(() -> {
            for (int i = 0; i < count; i++) {
                var username = randomUsername();
                UserEntity friendUserEntity = getDefaultUserEntity(username);
                userdataUserRepository.create(friendUserEntity);
                authUserRepository.create(getDefaultAuthUserEntity(username));

                userdataUserRepository.sendInvitation(friendUserEntity, UserEntity.fromJson(user));
            }

            return null;
        });
    }

    public AuthUserJson findAuthUserByUsername(String name) {
        Optional<AuthUserEntity> authEntity = authUserRepository.findByUsername(name);

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

    public static UserEntity getDefaultUserEntity(String username) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setFirstname(randomName());
        userEntity.setSurname(randomSurname());
        userEntity.setFullname(randomFullName());
        userEntity.setCurrency(randomCurrency());

        return userEntity;
    }

    public static AuthUserEntity getDefaultAuthUserEntity(String username) {
        AuthUserEntity userEntity = new AuthUserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword("123");
        userEntity.setEnabled(true);
        userEntity.setCredentialsNonExpired(true);
        userEntity.setAccountNonExpired(true);
        userEntity.setAccountNonLocked(true);

        List<AuthorityEntity> authorityEntities = Arrays.stream(Authority.values())
                .map(authority -> {
                    AuthorityEntity authorityEntity = new AuthorityEntity();
                    authorityEntity.setUser(userEntity);
                    authorityEntity.setAuthority(authority);

                    return authorityEntity;
                }).toList();
        userEntity.setAuthorities(authorityEntities);

        return userEntity;
    }

}
