package guru.qa.niffler.data.entity.auth;

import guru.qa.niffler.model.auth.AuthUserJson;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class AuthUserEntity {
    private UUID id;
    private String username;
    private String password;
    private Boolean enabled;
    private Boolean accountNonExpired;
    private Boolean accountNonLocked;
    private Boolean credentialsNonExpired;

    public AuthUserEntity fromJson(AuthUserJson json) {
        AuthUserEntity user = new AuthUserEntity();
        user.setId(json.id());
        user.setUsername(json.username());
        user.setPassword(json.password());
        user.setEnabled(json.enabled());
        user.setAccountNonExpired(json.accountNonExpired());
        user.setAccountNonLocked(json.accountNonLocked());
        user.setCredentialsNonExpired(json.credentialsNonExpired());

        return user;
    }
}
