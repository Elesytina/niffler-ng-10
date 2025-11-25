package guru.qa.niffler.service;

import guru.qa.niffler.data.dao.auth.impl.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.dao.auth.impl.AuthUserDaoJdbc;
import guru.qa.niffler.data.dao.userdata.UserdataUserDaoSpringJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.auth.AuthUserJson;
import guru.qa.niffler.model.auth.AuthorityJson;
import guru.qa.niffler.model.enums.Authority;
import guru.qa.niffler.model.userdata.UserJson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static guru.qa.niffler.helper.TestConstantHolder.CFG;
import static guru.qa.niffler.model.enums.Authority.read;
import static guru.qa.niffler.model.enums.Authority.write;
import static java.util.Arrays.stream;

public class UserDbClient {
    private final AuthUserDaoJdbc authUserDao = new AuthUserDaoJdbc();
    private final AuthAuthorityDaoJdbc authotiryDao = new AuthAuthorityDaoJdbc();
    private final UserdataUserDaoSpringJdbc udUserDao = new UserdataUserDaoSpringJdbc();

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userdataJdbcUrl());

    private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(CFG.authJdbcUrl());

    public UserJson createUserSpringJdbc(UserJson userJson, String password) {
        return xaTransactionTemplate.execute(() -> {
            var savedAuthUser = authUserDao.create(getAuthUserEntity(userJson, password));
            authotiryDao.create(
                    Stream.of(read, write)
                            .map(authority -> getAuthorityEntity(savedAuthUser, authority))
                            .toList());
            var user = udUserDao.create(UserEntity.fromJson(userJson));

            return UserJson.fromEntity(user);
        });
    }

    public void register(UserJson userJson, String password) {
        jdbcTxTemplate.execute(() -> {
            var username = userJson.username();
            Optional<AuthUserEntity> userEntity = authUserDao.findByUsername(username);

            if (userEntity.isPresent()) {
                throw new RuntimeException("User already exists");
            }
            AuthUserEntity entity = getAuthUserEntity(userJson, password);
            AuthUserEntity savedUser = authUserDao.create(entity);

            authotiryDao.create(stream(
                    Authority.values())
                    .map(a -> getAuthorityEntity(savedUser, a))
                    .toList());
            UserEntity udUserEntity = UserEntity.fromJson(userJson);
            udUserDao.create(udUserEntity);

            return UserJson.fromEntity(udUserEntity);
        });
    }

    public List<AuthorityJson> getAuthoritiesByUserId(UUID id) {
        List<AuthorityEntity> authorityEntities = authotiryDao.findAllByUserId(id);

        return authorityEntities.stream()
                .map(AuthorityJson::fromEntity)
                .toList();
    }

    public AuthUserJson getAuthUserByName(String name) {
        Optional<AuthUserEntity> authEntity = authUserDao.findByUsername(name);

        if (authEntity.isPresent()) {

            return AuthUserJson.fromEntity(authEntity.get());
        }
        throw new RuntimeException("Failed to find user");
    }

    private AuthUserEntity getAuthUserEntity(UserJson userJson, String password) {
        AuthUserEntity entity = new AuthUserEntity();
        entity.setUsername(userJson.username());
        entity.setPassword(password);
        entity.setEnabled(true);
        entity.setAccountNonExpired(true);
        entity.setAccountNonLocked(true);
        entity.setCredentialsNonExpired(true);

        return entity;
    }

    private AuthorityEntity getAuthorityEntity(AuthUserEntity savedEntity, Authority authority) {
        AuthorityEntity authorityEntity = new AuthorityEntity();
        authorityEntity.setAuthority(authority.name());
        authorityEntity.setUserId(savedEntity.getId());

        return authorityEntity;
    }

}
