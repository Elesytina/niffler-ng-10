package guru.qa.niffler.service;

import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.auth.AuthAuthorityDao;
import guru.qa.niffler.data.dao.auth.AuthUserDao;
import guru.qa.niffler.data.dao.auth.impl.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.dao.auth.impl.AuthUserDaoJdbc;
import guru.qa.niffler.data.dao.userdata.UserdataUserDaoJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.auth.AuthUserJson;
import guru.qa.niffler.model.auth.AuthorityJson;
import guru.qa.niffler.model.enums.Authority;
import guru.qa.niffler.model.enums.TrnIsolationLevel;
import guru.qa.niffler.model.userdata.UserJson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.Databases.getConnection;
import static guru.qa.niffler.data.Databases.xaTransaction;
import static guru.qa.niffler.helper.TestConstantHolder.CFG;
import static guru.qa.niffler.model.enums.Authority.read;
import static guru.qa.niffler.model.enums.Authority.write;
import static guru.qa.niffler.model.enums.TrnIsolationLevel.READ_COMMITED;

public class UserDbClient {

    public void register(UserJson userJson, String password) {
        xaTransaction(TrnIsolationLevel.REPEATABLE_READ,
                new Databases.XaConsumer(connect -> {
                    AuthUserDao userDao = new AuthUserDaoJdbc(connect);
                    AuthAuthorityDao authorityDao = new AuthAuthorityDaoJdbc(connect);
                    var username = userJson.username();
                    Optional<AuthUserEntity> userEntity = userDao.findByUsername(username);

                    if (userEntity.isPresent()) {
                        throw new RuntimeException("User already exists");
                    }

                    AuthUserEntity entity = getAuthUserEntity(userJson, password);
                    AuthUserEntity savedUser = userDao.create(entity);

                    AuthorityEntity authorityEntityRead = getAuthorityEntity(savedUser, read);
                    AuthorityEntity authorityEntityWrite = getAuthorityEntity(savedUser, write);
                    authorityDao.create(List.of(authorityEntityRead, authorityEntityWrite));

                }, CFG.authJdbcUrl()),

                new Databases.XaConsumer(connect -> {
                    UserEntity entity = UserEntity.fromJson(userJson);
                    new UserdataUserDaoJdbc(connect)
                            .create(entity);

                }, CFG.userdataJdbcUrl()));
    }

    public List<AuthorityJson> getAuthoritiesByUserId(UUID id) {
        AuthAuthorityDao authorityDao = new AuthAuthorityDaoJdbc(getConnection(CFG.authJdbcUrl(), READ_COMMITED));
        List<AuthorityEntity> authorityEntities = authorityDao.findAllByUserId(id);

        return authorityEntities.stream()
                .map(AuthorityJson::fromEntity)
                .toList();
    }

    public AuthUserJson getAuthUserByName(String name) {
        AuthUserDaoJdbc authUserDao = new AuthUserDaoJdbc(getConnection(CFG.authJdbcUrl(), READ_COMMITED));
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
