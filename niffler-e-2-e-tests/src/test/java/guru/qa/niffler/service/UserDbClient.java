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
import guru.qa.niffler.model.auth.AuthorityJson;
import guru.qa.niffler.model.enums.Authority;
import guru.qa.niffler.model.userdata.UserJson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.Databases.*;
import static guru.qa.niffler.helper.TestConstantHolder.CFG;
import static guru.qa.niffler.model.enums.Authority.read;
import static guru.qa.niffler.model.enums.Authority.write;
import static java.sql.Connection.TRANSACTION_READ_COMMITTED;

public class UserDbClient {

    public UserJson register(UserJson userJson, String password) {
        return transaction(
                new Databases.XaFunction<>(connect -> {
                    AuthUserDao userDao = new AuthUserDaoJdbc(connect);
                    AuthAuthorityDao authorityDao = new AuthAuthorityDaoJdbc(connect);
                    var username = userJson.username();
                    Optional<AuthUserEntity> userEntity = userDao.findByUsername(username);

                    if (userEntity.isPresent()) {
                        throw new RuntimeException("User already exists");
                    }

                    AuthUserEntity entity = getAuthUserEntity(userJson, password);
                    AuthUserEntity savedUser = userDao.save(entity);

                    AuthorityEntity authorityEntityRead = getAuthorityEntity(savedUser, read);
                    AuthorityEntity authorityEntityWrite = getAuthorityEntity(savedUser, write);
                    authorityDao.addAll(List.of(authorityEntityRead, authorityEntityWrite));

                    return userJson;
                }, CFG.authJdbcUrl(), TRANSACTION_READ_COMMITTED),

                new Databases.XaFunction<>(connect -> {
                    UserEntity entity = UserEntity.fromJson(userJson);
                    UserEntity userEntity = new UserdataUserDaoJdbc(connect)
                            .create(entity);

                    return UserJson.fromEntity(userEntity);
                }, CFG.userdataJdbcUrl(), TRANSACTION_READ_COMMITTED));
    }

    public List<AuthorityJson> getAuthoritiesById(UUID id) {
        AuthAuthorityDao authorityDao = new AuthAuthorityDaoJdbc(getConnection(CFG.authJdbcUrl(), TRANSACTION_READ_COMMITTED));
        List<AuthorityEntity> authorityEntities = authorityDao.findAllByUserId(id);

        return authorityEntities.stream()
                .map(AuthorityJson::fromEntity)
                .toList();
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
        authorityEntity.setAuthority(authority);
        authorityEntity.setUserId(savedEntity.getId());

        return authorityEntity;
    }

}
