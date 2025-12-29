package guru.qa.niffler.service.user;

import guru.qa.niffler.model.userdata.UserJson;

import java.util.UUID;

public interface UsersClient {

    UserJson create(String username, String password);

    UserJson update(UserJson userJson);

    void delete(UserJson userJson);

    UserJson findById(UUID id);

    UserJson findByUsername(String username);

    void addFriends(UserJson user, int count);

    void addIncomeInvitations(UserJson user, int count);

    void addOutcomeInvitations(UserJson user, int count);
}
