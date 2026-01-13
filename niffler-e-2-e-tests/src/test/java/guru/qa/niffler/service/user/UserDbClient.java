package guru.qa.niffler.service.user;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.auth.AuthUserRepository;
import guru.qa.niffler.data.repository.auth.impl.AuthUserRepositoryHiberImpl;
import guru.qa.niffler.data.repository.auth.impl.AuthUserRepositoryJdbcImpl;
import guru.qa.niffler.data.repository.auth.impl.AuthUserRepositorySpringJdbcImpl;
import guru.qa.niffler.data.repository.userdata.UserdataUserRepository;
import guru.qa.niffler.data.repository.userdata.impl.UserdataUserRepositoryHiberImpl;
import guru.qa.niffler.data.repository.userdata.impl.UserdataUserRepositoryJdbcImpl;
import guru.qa.niffler.data.repository.userdata.impl.UserdataUserRepositorySpringImpl;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.enums.Authority;
import guru.qa.niffler.model.enums.RelationType;
import guru.qa.niffler.model.enums.RepositoryImplType;
import guru.qa.niffler.model.userdata.UserJson;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.helper.TestConstantHolder.CFG;
import static guru.qa.niffler.helper.TestConstantHolder.DEFAULT_PASSWORD;
import static guru.qa.niffler.utils.RandomDataUtils.randomCurrency;
import static guru.qa.niffler.utils.RandomDataUtils.randomFullName;
import static guru.qa.niffler.utils.RandomDataUtils.randomName;
import static guru.qa.niffler.utils.RandomDataUtils.randomSurname;
import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

@Slf4j
public class UserDbClient implements UsersClient {

    private AuthUserRepository authUserRepository;

    private UserdataUserRepository userdataUserRepository;

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userdataJdbcUrl());

    public UserDbClient(RepositoryImplType type) {
        switch (type) {
            case JDBC -> {
                authUserRepository = new AuthUserRepositoryJdbcImpl();
                userdataUserRepository = new UserdataUserRepositoryJdbcImpl();
            }
            case SPRING_JDBC -> {
                authUserRepository = new AuthUserRepositorySpringJdbcImpl();
                userdataUserRepository = new UserdataUserRepositorySpringImpl();
            }
            case HIBERNATE -> {
                authUserRepository = new AuthUserRepositoryHiberImpl();
                userdataUserRepository = new UserdataUserRepositoryHiberImpl();
            }
        }
    }

    @Override
    public UserJson create(String username, String password) {
        return xaTransactionTemplate.execute(() -> {
            AuthUserEntity authUserEntity = getDefaultAuthUserEntity(username, DEFAULT_PASSWORD);

            authUserRepository.create(authUserEntity);

            UserEntity userEntity = getDefaultUserEntity(username);

            var user = userdataUserRepository.create(userEntity);

            return UserJson.fromEntity(user);
        });
    }

    @Override
    public UserJson update(UserJson userJson) {
        return xaTransactionTemplate.execute(() -> {
            Optional<UserEntity> userEntity = userdataUserRepository.findById(userJson.id());

            if (userEntity.isPresent()) {
                var user = userdataUserRepository.update(UserEntity.fromJson(userJson));

                return UserJson.fromEntity(user);
            } else {
                throw new RuntimeException("User with id %s not found".formatted(userJson.id()));
            }
        });
    }

    public void delete(UserJson userJson) {
        xaTransactionTemplate.execute(() -> {
            Optional<UserEntity> userEntity = userdataUserRepository.findById(userJson.id());
            Optional<AuthUserEntity> authUser = authUserRepository.findByUsername(userJson.username());

            if (userEntity.isPresent()) {
                userdataUserRepository.remove(userEntity.get());

                authUser.ifPresent(authUserEntity -> authUserRepository.remove(authUserEntity.getId()));

                return null;
            } else {
                throw new RuntimeException("User with id %s not found".formatted(userJson.id()));
            }
        });
    }

    public UserJson findById(UUID id) {
        Optional<UserEntity> userEntity = userdataUserRepository.findById(id);

        if (userEntity.isPresent()) {

            return UserJson.fromEntity(userEntity.get());
        }
        throw new RuntimeException("Failed to find user by id: %s".formatted(id));
    }

    @Override
    public UserJson findByUsername(String username) {
        Optional<UserEntity> userEntity = userdataUserRepository.findByUsername(username);

        if (userEntity.isPresent()) {

            return UserJson.fromEntity(userEntity.get());
        }
        throw new RuntimeException("Failed to find user by username: %s".formatted(username));
    }

    @Override
    public List<UserJson> addFriends(UserJson user, int count) {
        return addRelations(user, RelationType.FRIENDSHIP, count);
    }

    @Override
    public List<UserJson> addIncomeInvitations(UserJson user, int count) {
        return addRelations(user, RelationType.INCOME_INVITATION, count);
    }

    @Override
    public List<UserJson> addOutcomeInvitations(UserJson user, int count) {
        return addRelations(user, RelationType.OUTCOME_INVITATION, count);
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

    public static AuthUserEntity getDefaultAuthUserEntity(String username, String password) {
        AuthUserEntity userEntity = new AuthUserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword(password);
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

    private List<UserJson> addRelations(UserJson user, RelationType relationType, int count) {
        List<UserJson> friends = new ArrayList<>();

        return xaTransactionTemplate.execute(() -> {
            Optional<UserEntity> userEntity = userdataUserRepository.findById(user.id());

            if (userEntity.isPresent()) {
                for (int i = 0; i < count; i++) {
                    var username = randomUsername();
                    UserEntity friendUserEntity = getDefaultUserEntity(username);
                    userdataUserRepository.create(friendUserEntity);
                    authUserRepository.create(getDefaultAuthUserEntity(username, "123"));

                    switch (relationType) {
                        case FRIENDSHIP ->
                                userdataUserRepository.addFriend(friendUserEntity, UserEntity.fromJson(user));
                        case INCOME_INVITATION ->
                                userdataUserRepository.sendInvitation(UserEntity.fromJson(user), friendUserEntity);
                        case OUTCOME_INVITATION ->
                                userdataUserRepository.sendInvitation(friendUserEntity, UserEntity.fromJson(user));
                    }
                    friends.add(UserJson.fromEntity(friendUserEntity));
                }

                return friends;
            } else {
                throw new RuntimeException("User with id %s not found".formatted(user.id()));
            }
        });
    }

}
