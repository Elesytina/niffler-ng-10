package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.TestData;
import guru.qa.niffler.model.userdata.UserJson;
import guru.qa.niffler.service.user.UserDbClient;
import guru.qa.niffler.service.user.UsersClient;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static guru.qa.niffler.jupiter.extension.TestMethodContextExtension.context;
import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

public class UserExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UserExtension.class);
    public static final String DEFAULT_PASSWORD = "123";

    private final UsersClient usersClient = new UserDbClient();

    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(userAnno -> {
                            if ("".equals(userAnno.username())) {
                                final String username = randomUsername();
                                final UserJson user = usersClient.create(username, DEFAULT_PASSWORD);
                                final List<UserJson> incomeInvitations = usersClient.addIncomeInvitations(user, userAnno.incomeInvitationsCount());
                                final List<UserJson> outcomeInvitations = usersClient.addOutcomeInvitations(user, userAnno.outcomeInvitationsCount());
                                final List<UserJson> friends = usersClient.addFriends(user, userAnno.friendsCount());

                                final TestData testData = new TestData(
                                        DEFAULT_PASSWORD,
                                        incomeInvitations,
                                        outcomeInvitations,
                                        friends,
                                        new ArrayList<>(),
                                        new ArrayList<>()
                                );

                                context.getStore(NAMESPACE).put(
                                        context.getUniqueId(),
                                        user.addTestData(testData)
                                );
                            }
                        }
                );
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws
            ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(UserJson.class);
    }

    @Override
    public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws
            ParameterResolutionException {
        return createdUser().orElseThrow();
    }

    public static Optional<UserJson> createdUser() {
        final ExtensionContext methodContext = context();

        return Optional.ofNullable(methodContext.getStore(NAMESPACE)
                .get(methodContext.getUniqueId(), UserJson.class));
    }
}