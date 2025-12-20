package guru.qa.niffler.model.auth;

import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.model.enums.Authority;

import java.util.UUID;

public record AuthorityJson(
        UUID id,
        Authority authority,
        UUID userId) {

    public static AuthorityJson fromEntity(AuthorityEntity entity) {

        return new AuthorityJson(
                entity.getId(),
                entity.getAuthority(),
                entity.getUser().getId());
    }

}
