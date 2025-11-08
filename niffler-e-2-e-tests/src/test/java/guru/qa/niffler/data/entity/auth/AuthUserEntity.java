package guru.qa.niffler.data.entity.auth;

import lombok.Getter;

import java.util.UUID;

@Getter
public class AuthUserEntity {
    private UUID id;
    private String username;
    private String password;
    private Boolean enabled;
    private Boolean accountNonExpired;
    private Boolean accountNonLocked;
    private Boolean credentialsNonExpired;
}
