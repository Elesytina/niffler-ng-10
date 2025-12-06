package guru.qa.niffler.service.user;

import guru.qa.niffler.data.dao.auth.AuthAuthorityDao;
import guru.qa.niffler.data.dao.auth.impl.AuthAuthoritySpringDaoJdbc;
import guru.qa.niffler.data.dao.auth.impl.AuthUserSpringDaoJdbc;
import guru.qa.niffler.data.dao.userdata.UserdataUserDao;
import guru.qa.niffler.data.dao.userdata.UserdataUserDaoSpringJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.auth.AuthUserJson;
import guru.qa.niffler.model.auth.AuthorityJson;
import guru.qa.niffler.model.enums.Authority;
import guru.qa.niffler.model.userdata.UserJson;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static guru.qa.niffler.helper.TestConstantHolder.CFG;
import static guru.qa.niffler.model.enums.Authority.read;
import static guru.qa.niffler.model.enums.Authority.write;

@Slf4j
public class UserDbClient {
    private final AuthUserSpringDaoJdbc authUserDaoSpring = new AuthUserSpringDaoJdbc();
    private final AuthAuthorityDao authorityDaoSpring = new AuthAuthoritySpringDaoJdbc();
    private final UserdataUserDao udUserDaoSpring = new UserdataUserDaoSpringJdbc();

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userdataJdbcUrl());

    public UserJson createUserSpringJdbc(UserJson userJson, String password) {
        return xaTransactionTemplate.execute(() -> {
            var savedAuthUser = authUserDaoSpring.create(getAuthUserEntity(userJson, password));
            authorityDaoSpring.create(
                    Stream.of(read, write)
                            .map(authority -> getAuthorityEntity(savedAuthUser, authority))
                            .toList());
            var user = udUserDaoSpring.create(UserEntity.fromJson(userJson));

            return UserJson.fromEntity(user);
        });
    }

    public List<AuthorityJson> getAuthoritiesByUserId(UUID id) {
        List<AuthorityEntity> authorityEntities = authorityDaoSpring.findAllByUserId(id);

        return authorityEntities.stream()
                .map(AuthorityJson::fromEntity)
                .toList();
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
        authorityEntity.setAuthority(authority.name());
        authorityEntity.setUserId(savedEntity.getId());

        return authorityEntity;
    }

}
