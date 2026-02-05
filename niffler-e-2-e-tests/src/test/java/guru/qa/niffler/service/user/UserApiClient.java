package guru.qa.niffler.service.user;

import guru.qa.niffler.api.UsersApi;
import guru.qa.niffler.model.userdata.UserJson;
import guru.qa.niffler.service.RestClient;
import guru.qa.niffler.service.auth.AuthApiClient;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Assertions;
import retrofit2.Response;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static guru.qa.niffler.helper.TestConstantHolder.CFG;
import static guru.qa.niffler.helper.TestConstantHolder.DEFAULT_PASSWORD;

@ParametersAreNonnullByDefault
public class UserApiClient extends RestClient implements UsersClient {

    private final AuthApiClient authApiClient = new AuthApiClient();

    private final UsersApi usersApi = create(UsersApi.class);

    public UserApiClient() {
        super(CFG.userdataUrl());
    }

    @Override
    public @Nonnull UserJson create(String username, String password) {
        authApiClient.register(username, password);
        try {
            Response<UserJson> responseInfo = usersApi.currentUser(username)
                    .execute();
            Assertions.assertEquals(200, responseInfo.code(), "Unexpected response code");

            return Objects.requireNonNull(responseInfo.body());
        } catch (IOException exception) {
            throw new AssertionError(exception);
        }
    }

    @Override
    public @Nonnull UserJson update(UserJson userJson) {
        try {
            Response<UserJson> response = usersApi.updateUserInfo(userJson)
                    .execute();
            Assertions.assertEquals(200, response.code(), "Unexpected response code");

            return Objects.requireNonNull(response.body());
        } catch (IOException exception) {
            throw new AssertionError(exception);
        }
    }

    @Override
    public @Nullable UserJson findByUsername(String username) {
        try {
            Response<UserJson> response = usersApi.currentUser(username)
                    .execute();
            Assertions.assertEquals(200, response.code(), "Unexpected response code");

            return response.body();
        } catch (IOException exception) {
            throw new AssertionError(exception);
        }
    }


    public @Nullable List<UserJson> findAllByUsername(String username, String searchQuery) {
        try {
            Response<List<UserJson>> response = usersApi.allUsers(username, searchQuery)
                    .execute();
            Assertions.assertEquals(200, response.code(), "Unexpected response code");

            return response.body();
        } catch (IOException exception) {
            throw new AssertionError(exception);
        }
    }

    @Override
    public @Nonnull List<UserJson> addFriends(UserJson user, int count) {
        try {
            var username = user.username();
            List<UserJson> friends = new ArrayList<>();
            for (int counter = 0; counter < count; counter++) {
                String friendName = RandomDataUtils.randomUsername();
                authApiClient.register(friendName, DEFAULT_PASSWORD);

                Response<UserJson> invitationResponse = usersApi.sendInvitation(friendName, username).execute();
                Assertions.assertEquals(200, invitationResponse.code(), "Unexpected response code");

                Response<UserJson> acceptResponse = usersApi.acceptInvitation(username, friendName).execute();
                Assertions.assertEquals(200, acceptResponse.code(), "Unexpected response code");

                friends.add(acceptResponse.body());
            }

            return friends;
        } catch (IOException exception) {
            throw new AssertionError(exception);
        }
    }

    @Override
    public @Nonnull List<UserJson> addIncomeInvitations(UserJson user, int count) {
        try {
            var username = user.username();
            List<UserJson> incomeInvitations = new ArrayList<>();
            for (int counter = 0; counter < count; counter++) {
                String friendName = RandomDataUtils.randomUsername();
                authApiClient.register(friendName, DEFAULT_PASSWORD);

                Response<UserJson> invitationResponse = usersApi.sendInvitation(friendName, username).execute();
                Assertions.assertEquals(200, invitationResponse.code(), "Unexpected response code");

                incomeInvitations.add(invitationResponse.body());
            }

            return incomeInvitations;
        } catch (IOException exception) {
            throw new AssertionError(exception);
        }
    }

    @Override
    public @Nonnull List<UserJson> addOutcomeInvitations(UserJson user, int count) {
        try {
            var username = user.username();
            List<UserJson> outcomeInvitations = new ArrayList<>();
            for (int counter = 0; counter < count; counter++) {
                String friendName = RandomDataUtils.randomUsername();
                authApiClient.register(friendName, DEFAULT_PASSWORD);

                Response<UserJson> invitationResponse = usersApi.sendInvitation(username, friendName).execute();
                Assertions.assertEquals(200, invitationResponse.code(), "Unexpected response code");

                outcomeInvitations.add(invitationResponse.body());
            }

            return outcomeInvitations;
        } catch (IOException exception) {
            throw new AssertionError(exception);
        }
    }
}
