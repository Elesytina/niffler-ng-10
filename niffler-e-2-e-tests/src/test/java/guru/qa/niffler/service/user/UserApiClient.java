package guru.qa.niffler.service.user;

import guru.qa.niffler.api.AuthApi;
import guru.qa.niffler.api.UsersApi;
import guru.qa.niffler.model.userdata.UserJson;
import guru.qa.niffler.utils.RandomDataUtils;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.junit.jupiter.api.Assertions;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static guru.qa.niffler.helper.TestConstantHolder.CFG;
import static guru.qa.niffler.helper.TestConstantHolder.DEFAULT_PASSWORD;

@ParametersAreNonnullByDefault
public class UserApiClient implements UsersClient {

    private static final CookieManager cm = new CookieManager(null, CookiePolicy.ACCEPT_ALL);

    private static final HttpLoggingInterceptor logging = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY);

    private static final OkHttpClient client = new OkHttpClient().newBuilder()
            .addInterceptor(logging)
            .cookieJar(new JavaNetCookieJar(cm))
            .build();

    private final Retrofit udRetrofit = new Retrofit.Builder()
            .client(client)
            .baseUrl(CFG.userdataUrl())
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final Retrofit authRetrofit = new Retrofit.Builder()
            .client(client)
            .baseUrl(CFG.authUrl())
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final AuthApi authApi = authRetrofit.create(AuthApi.class);

    private final UsersApi usersApi = udRetrofit.create(UsersApi.class);

    @Override
    public @Nonnull UserJson create(String username, String password) {
        try {
            Response<Void> response = authApi.requestRegisterForm().execute();
            Assertions.assertEquals(200, response.code(), "Unexpected response code");

            Optional<HttpCookie> httpCookie = cm.getCookieStore().getCookies()
                    .stream()
                    .filter(c -> c.getName().equals("XSRF-TOKEN"))
                    .findFirst();
            var token = httpCookie.map(HttpCookie::getValue).orElseThrow();

            Response<Void> registerResponse = authApi.register(
                    username,
                    password,
                    password,
                    token).execute();
            Assertions.assertEquals(200, registerResponse.code(), "Unexpected response code");

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
    public @Nonnull UserJson findByUsername(String username) {
        try {
            Response<UserJson> response = usersApi.currentUser(username)
                    .execute();
            Assertions.assertEquals(200, response.code(), "Unexpected response code");

            return Objects.requireNonNull(response.body());
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
                Response<Void> registerResponse = authApi.register(friendName, DEFAULT_PASSWORD, DEFAULT_PASSWORD, null).execute();
                Assertions.assertEquals(200, registerResponse.code(), "Unexpected response code");

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
                Response<Void> registerResponse = authApi.register(friendName, DEFAULT_PASSWORD, DEFAULT_PASSWORD, null).execute();
                Assertions.assertEquals(200, registerResponse.code(), "Unexpected response code");

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
                Response<Void> registerResponse = authApi.register(friendName, DEFAULT_PASSWORD, DEFAULT_PASSWORD, null).execute();
                Assertions.assertEquals(200, registerResponse.code(), "Unexpected response code");

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
