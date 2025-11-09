package guru.qa.niffler.model;

import guru.qa.niffler.model.auth.AuthUserJson;
import guru.qa.niffler.model.auth.AuthorityJson;
import guru.qa.niffler.model.userdata.UserJson;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JoinedUserJson {
    private UserJson userJson;
    private AuthUserJson authUserJson;
    private List<AuthorityJson> authorityJsonList;
}
