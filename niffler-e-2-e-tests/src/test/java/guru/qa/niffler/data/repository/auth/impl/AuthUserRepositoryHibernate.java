package guru.qa.niffler.data.repository.auth.impl;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.repository.auth.AuthUserRepository;
import jakarta.persistence.EntityManager;

import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.jpa.EntityMangers.em;
import static guru.qa.niffler.helper.TestConstantHolder.CFG;

public class AuthUserRepositoryHibernate implements AuthUserRepository {

    private final EntityManager em = em(CFG.authJdbcUrl());

    @Override
    public Optional<AuthUserEntity> findById(UUID id) {
        return Optional.ofNullable(em.find(AuthUserEntity.class, id));
    }

    @Override
    public AuthUserEntity create(AuthUserEntity authUser) {
        em.joinTransaction();
        em.persist(authUser);
        return authUser;
    }

    public Optional<AuthUserEntity> findByUsername(String name) {
        var sql = "FROM AuthUserEntity au WHERE au.username =:username";

        AuthUserEntity authUser = em.createQuery(
                        sql, AuthUserEntity.class)
                .setParameter("username", name)
                .getSingleResult();

        return Optional.ofNullable(authUser);
    }
}
