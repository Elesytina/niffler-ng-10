package guru.qa.niffler.service.user;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.auth.AuthUserRepository;
import guru.qa.niffler.data.repository.auth.impl.AuthUserRepositoryHiberImpl;
import guru.qa.niffler.data.repository.userdata.UserdataUserRepository;
import guru.qa.niffler.data.repository.userdata.impl.UserdataUserRepositoryHiberImpl;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.enums.Authority;
import guru.qa.niffler.model.userdata.UserJson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.helper.TestConstantHolder.CFG;
import static guru.qa.niffler.service.user.UserDbClient.getDefaultAuthUserEntity;
import static guru.qa.niffler.service.user.UserDbClient.getDefaultUserEntity;
import static guru.qa.niffler.utils.RandomDataUtils.*;

@Slf4j
public class UserDbClientHibernate {
    private final AuthUserRepository authUserRepository = new AuthUserRepositoryHiberImpl();
    private final UserdataUserRepository userdataUserRepository = new UserdataUserRepositoryHiberImpl();
    private static final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();


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
        return xaTransactionTemplate.execute(() -> {
            UserEntity updated = userdataUserRepository.update(UserEntity.fromJson(userJson));

            return UserJson.fromEntity(updated);
        });
    }

    public UserJson findById(UUID id) {
        Optional<UserEntity> userEntity = userdataUserRepository.findById(id);

        if (userEntity.isPresent()) {

            return UserJson.fromEntity(userEntity.get());
        }
        throw new RuntimeException("Failed to find user by id: %s".formatted(id));
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
                }
        );
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

    public UserJson findByUsername(String name) {
        Optional<UserEntity> userEntity = userdataUserRepository.findByUsername(name);

        if (userEntity.isPresent()) {

            return UserJson.fromEntity(userEntity.get());
        }
        throw new RuntimeException("Failed to find user");
    }

    public static AuthUserEntity getAuthUserEntity(UserJson userJson, String password) {
        AuthUserEntity entity = new AuthUserEntity();
        var username = userJson.username();
        entity.setUsername(username);
        entity.setPassword(passwordEncoder.encode(password));
        entity.setEnabled(true);
        entity.setAccountNonExpired(true);
        entity.setAccountNonLocked(true);
        entity.setCredentialsNonExpired(true);
        entity.setAuthorities(
                Arrays.stream(Authority.values()).map(a -> {
                    AuthorityEntity authorityEntity = new AuthorityEntity();
                    authorityEntity.setAuthority(a);
                    authorityEntity.setUser(entity);
                    return authorityEntity;
                }).toList());

        return entity;
    }

    public void delete(UserJson userJson) {
        xaTransactionTemplate.execute(() -> {
            var username = userJson.username();
            Optional<AuthUserEntity> authUser = authUserRepository.findByUsername(username);
            log.info(authUser.toString());

            if (authUser.isPresent()) {
                authUserRepository.remove(authUser.get().getId());
            } else {
                throw new RuntimeException("Failed to find auth user");
            }
            var userId = userJson.id();
            Optional<UserEntity> userEntity = userdataUserRepository.findById(userId);

            if (userEntity.isPresent()) {
                log.info(userEntity.toString());
                userdataUserRepository.remove(userEntity.get());
            } else {
                throw new RuntimeException("Failed to find auth user");
            }

            return null;
        });
    }

}
