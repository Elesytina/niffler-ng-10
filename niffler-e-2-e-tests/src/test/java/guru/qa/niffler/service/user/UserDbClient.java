package guru.qa.niffler.service;

import guru.qa.niffler.data.dao.auth.AuthAuthorityDao;
import guru.qa.niffler.data.dao.auth.AuthUserDao;
import guru.qa.niffler.data.dao.auth.impl.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.dao.auth.impl.AuthAuthoritySpringDaoJdbc;
import guru.qa.niffler.data.dao.auth.impl.AuthUserDaoJdbc;
import guru.qa.niffler.data.dao.auth.impl.AuthUserSpringDaoJdbc;
import guru.qa.niffler.data.dao.userdata.UserdataUserDao;
import guru.qa.niffler.data.dao.userdata.UserdataUserDaoJdbc;
import guru.qa.niffler.data.dao.userdata.UserdataUserDaoSpringJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.tpl.DataSources;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.auth.AuthUserJson;
import guru.qa.niffler.model.auth.AuthorityJson;
import guru.qa.niffler.model.enums.Authority;
import guru.qa.niffler.model.userdata.UserJson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static guru.qa.niffler.helper.TestConstantHolder.CFG;
import static guru.qa.niffler.model.enums.Authority.read;
import static guru.qa.niffler.model.enums.Authority.write;
import static java.util.Arrays.stream;

@Slf4j
public class UserDbClient {
    private final AuthUserSpringDaoJdbc authUserDaoSpring = new AuthUserSpringDaoJdbc();
    private final AuthAuthorityDao authorityDaoSpring = new AuthAuthoritySpringDaoJdbc();
    private final UserdataUserDao udUserDaoSpring = new UserdataUserDaoSpringJdbc();

    private final AuthUserDao authUserDao = new AuthUserDaoJdbc();
    private final AuthAuthorityDao authorityDao = new AuthAuthorityDaoJdbc();
    private final UserdataUserDao udUserDao = new UserdataUserDaoJdbc();

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userdataJdbcUrl());

    private final TransactionTemplate chainTransactionTemplate = new TransactionTemplate(
            new ChainedTransactionManager(
                    new JdbcTransactionManager(DataSources.getDataSource(CFG.authJdbcUrl())),
                    new JdbcTransactionManager(DataSources.getDataSource(CFG.userdataJdbcUrl()))
            ));

    private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(CFG.authJdbcUrl());

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

    public void register(UserJson userJson, String password) {
        jdbcTxTemplate.execute(() -> {
            var username = userJson.username();
            Optional<AuthUserEntity> userEntity = authUserDao.findByUsername(username);

            if (userEntity.isPresent()) {
                throw new RuntimeException("User already exists");
            }
            AuthUserEntity entity = getAuthUserEntity(userJson, password);
            AuthUserEntity savedUser = authUserDao.create(entity);

            authorityDao.create(stream(
                    Authority.values())
                    .map(a -> getAuthorityEntity(savedUser, a))
                    .toList());
            UserEntity udUserEntity = UserEntity.fromJson(userJson);
            udUserDao.create(udUserEntity);

            return UserJson.fromEntity(udUserEntity);
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

    //внутренняя транзакция
    public AuthUserEntity createUserAndAuthorityWithChain(UserJson userJson, String password) {
        return chainTransactionTemplate.execute(status -> {
            var savedAuthUser = authUserDaoSpring.create(getAuthUserEntity(userJson, password));

            authorityDaoSpring.create(
                    Stream.of(read, write)
                            .map(authority -> getAuthorityEntity(savedAuthUser, authority))
                            .toList());
            return savedAuthUser;
        });
    }

    //внешняя транзакция
    public void createUserWithChainedTransactionManager(UserJson userJson, String password) {
        chainTransactionTemplate.execute(status -> {
            AuthUserEntity authUserEntity = createUserAndAuthorityWithChain(userJson, password);

            UserEntity user = udUserDaoSpring.create(UserEntity.fromJson(userJson));

            //simulate error
            if (user != null) {
                throw new RuntimeException("Error simulation");
            }
            return authUserEntity;
        });
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
