package guru.qa.niffler.service.user;

import guru.qa.niffler.model.userdata.UserJson;

import java.util.List;

public interface UsersClient {

    UserJson create(String username, String password);

    UserJson update(UserJson userJson);

    UserJson findByUsername(String username);

    List<UserJson> addFriends(UserJson user, int count);

    List<UserJson> addIncomeInvitations(UserJson user, int count);

    List<UserJson> addOutcomeInvitations(UserJson user, int count);
}
