package guru.qa.niffler.data.entity.auth;

import guru.qa.niffler.model.auth.AuthorityJson;
import guru.qa.niffler.model.enums.Authority;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
public class AuthorityEntity {
    private UUID id;
    private String authority;
    private UUID userId;

    public static AuthorityEntity fromJson(AuthorityJson json) {
        AuthorityEntity entity = new AuthorityEntity();
        entity.setId(json.id());
        entity.setAuthority(json.authority());
        entity.setUserId(json.userId());

        return entity;
    }
}
