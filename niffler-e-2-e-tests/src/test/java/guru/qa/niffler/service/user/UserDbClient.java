package guru.qa.niffler.service.user;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.auth.AuthUserRepository;
import guru.qa.niffler.data.repository.userdata.UserdataUserRepository;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.enums.Authority;
import guru.qa.niffler.model.enums.RelationType;
import guru.qa.niffler.model.userdata.UserJson;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.helper.TestConstantHolder.CFG;
import static guru.qa.niffler.helper.TestConstantHolder.DEFAULT_PASSWORD;
import static guru.qa.niffler.helper.TestConstantHolder.PASSWORD_ENCODER;
import static guru.qa.niffler.model.enums.RelationType.FRIENDSHIP;
import static guru.qa.niffler.model.enums.RelationType.INCOME_INVITATION;
import static guru.qa.niffler.model.enums.RelationType.OUTCOME_INVITATION;
import static guru.qa.niffler.utils.RandomDataUtils.randomCurrency;
import static guru.qa.niffler.utils.RandomDataUtils.randomFullName;
import static guru.qa.niffler.utils.RandomDataUtils.randomName;
import static guru.qa.niffler.utils.RandomDataUtils.randomSurname;
import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

@Slf4j
@ParametersAreNonnullByDefault
public class UserDbClient implements UsersClient {

    private final AuthUserRepository authUserRepository = AuthUserRepository.getInstance();

    private final UserdataUserRepository userdataUserRepository = UserdataUserRepository.getInstance();

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userdataJdbcUrl());

    @Step("create user")
    @Override
    public @Nonnull UserJson create(String username, String password) {
        return Objects.requireNonNull(xaTransactionTemplate.execute(() -> {
            AuthUserEntity authUserEntity = getDefaultAuthUserEntity(username, DEFAULT_PASSWORD);
            authUserRepository.create(authUserEntity);

            UserEntity userEntity = getDefaultUserEntity(username);
            var created = userdataUserRepository.create(userEntity);

            return UserJson.fromEntity(created);
        }));
    }

    @Step("update user")
    @Override
    public @Nonnull UserJson update(UserJson userJson) {
        return Objects.requireNonNull(xaTransactionTemplate.execute(() -> {
            Optional<UserEntity> userEntity = userdataUserRepository.findById(userJson.id());

            if (userEntity.isPresent()) {
                var updated = userdataUserRepository.update(UserEntity.fromJson(userJson));

                return UserJson.fromEntity(updated);
            } else {
                throw new RuntimeException("User with id %s not found".formatted(userJson.id()));
            }
        }));
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

    public @Nonnull UserJson findById(UUID id) {
        Optional<UserEntity> userEntity = userdataUserRepository.findById(id);

        if (userEntity.isPresent()) {

            return UserJson.fromEntity(userEntity.get());
        }
        throw new RuntimeException("Failed to find user by id: %s".formatted(id));
    }

    @Override
    public @Nonnull UserJson findByUsername(String username) {
        Optional<UserEntity> userEntity = userdataUserRepository.findByUsername(username);

        if (userEntity.isPresent()) {

            return UserJson.fromEntity(userEntity.get());
        }
        throw new RuntimeException("Failed to find user by username: %s".formatted(username));
    }

    @Override
    public @Nonnull List<UserJson> addFriends(UserJson user, int count) {
        return addRelations(user, FRIENDSHIP, count);
    }

    @Override
    public @Nonnull List<UserJson> addIncomeInvitations(UserJson user, int count) {
        return addRelations(user, INCOME_INVITATION, count);
    }

    @Override
    public @Nonnull List<UserJson> addOutcomeInvitations(UserJson user, int count) {
        return addRelations(user, OUTCOME_INVITATION, count);
    }

    public static @Nonnull UserEntity getDefaultUserEntity(String username) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setFirstname(randomName());
        userEntity.setSurname(randomSurname());
        userEntity.setFullname(randomFullName());
        userEntity.setCurrency(randomCurrency());

        return userEntity;
    }

    public static @Nonnull AuthUserEntity getDefaultAuthUserEntity(String username, String password) {
        AuthUserEntity userEntity = new AuthUserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword(PASSWORD_ENCODER.encode(password));
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

    private @Nonnull List<UserJson> addRelations(UserJson user, RelationType relationType, int count) {
        List<UserJson> friends = new ArrayList<>();

        return Objects.requireNonNull(xaTransactionTemplate.execute(() -> {
            Optional<UserEntity> foundUserEntity = userdataUserRepository.findById(user.id());

            if (foundUserEntity.isPresent()) {
                for (int i = 0; i < count; i++) {
                    var username = randomUsername();
                    UserEntity friendUserEntity = getDefaultUserEntity(username);
                    UserEntity createdFriend = userdataUserRepository.create(friendUserEntity);
                    authUserRepository.create(getDefaultAuthUserEntity(username, DEFAULT_PASSWORD));

                    UserEntity userEntity = foundUserEntity.get();
                    switch (relationType) {
                        case FRIENDSHIP -> userdataUserRepository.addFriend(createdFriend, userEntity);
                        case INCOME_INVITATION -> userdataUserRepository.sendInvitation(userEntity, createdFriend);
                        case OUTCOME_INVITATION -> userdataUserRepository.sendInvitation(createdFriend, userEntity);
                    }
                    friends.add(UserJson.fromEntity(createdFriend));
                }

                return friends;
            } else {
                throw new RuntimeException("User with id %s not found".formatted(user.id()));
            }
        }));
    }

}
