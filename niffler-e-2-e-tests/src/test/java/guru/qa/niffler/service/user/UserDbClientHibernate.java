package guru.qa.niffler.service.user;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.auth.AuthUserRepository;
import guru.qa.niffler.data.repository.auth.impl.AuthUserRepositoryHiberImpl;
import guru.qa.niffler.data.repository.userdata.UserdataUserRepository;
import guru.qa.niffler.data.repository.userdata.impl.UserdataUserRepositoryHiberImpl;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.auth.AuthUserJson;
import guru.qa.niffler.model.enums.Authority;
import guru.qa.niffler.model.userdata.UserJson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.helper.TestConstantHolder.CFG;
import static guru.qa.niffler.utils.RandomDataUtils.*;

@Slf4j
public class UserDbClientHibernate {
    private final AuthUserRepository authUserRepository = new AuthUserRepositoryHiberImpl();
    private final UserdataUserRepository userdataUserRepository = new UserdataUserRepositoryHiberImpl();
    private static final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();


    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userdataJdbcUrl());

    public UserJson createUser(String username, String password) {
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
        Optional<AuthUserEntity> authEntity = authUserRepository.findByUsername(name);

        if (authEntity.isPresent()) {

            return AuthUserJson.fromEntity(authEntity.get());
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

}
