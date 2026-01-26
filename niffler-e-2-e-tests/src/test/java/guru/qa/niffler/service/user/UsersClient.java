package guru.qa.niffler.service.user;

import guru.qa.niffler.model.userdata.UserJson;
import io.qameta.allure.Step;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public interface UsersClient {

    @Step("create new user {username} {password}")
    UserJson create(String username, String password);

    @Step("update user information")
    UserJson update(UserJson userJson);

    @Step("find user by username {username}")
    UserJson findByUsername(String username);

    @Step("add {count} friends for user {user}")
    List<UserJson> addFriends(UserJson user, int count);

    @Step("add {count} income invitations for user")
    List<UserJson> addIncomeInvitations(UserJson user, int count);

    @Step("add {count} outcome invitations for user")
    List<UserJson> addOutcomeInvitations(UserJson user, int count);
}
