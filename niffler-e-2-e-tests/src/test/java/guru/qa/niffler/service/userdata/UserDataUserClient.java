package guru.qa.niffler.service.userdata;

import guru.qa.niffler.model.UserJson;

import java.util.UUID;

public interface UserDataUserClient {

    UserJson getUserById(UUID id);

    UserJson getUserByUsername(String userName);

    UserJson createUser(UserJson spend);

    void deleteUser(UserJson spend);
}
