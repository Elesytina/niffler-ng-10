package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.UserType;
import io.qameta.allure.Allure;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import static guru.qa.niffler.jupiter.annotation.UserType.Type;
@Deprecated
public class UsersQueueExtension implements
        BeforeTestExecutionCallback,
        AfterTestExecutionCallback,
        ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UsersQueueExtension.class);

    public record StaticUser(String username, String password, String friend, String income, String outcome) {
    }

    private static final Queue<StaticUser> EMPTY_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_FRIEND_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_INCOME_REQUEST_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_OUTCOME_REQUEST_USERS = new ConcurrentLinkedQueue<>();

    static {
        EMPTY_USERS.add(new StaticUser("marylou.johnson", "password", null, null, null));
        WITH_FRIEND_USERS.addAll(List.of(
                        new StaticUser("garfield.skiles", "password", "fishka", null, null),
                        new StaticUser("fishka", "Querty67", "v", null, null)
                )
        );
        WITH_INCOME_REQUEST_USERS.addAll(
                List.of(
                        new StaticUser("winfred.haag", "password", null, "fishka", null),
                        new StaticUser("ivan", "111", null, "shani.marquardt", null)
                )
        );
        WITH_OUTCOME_REQUEST_USERS.addAll(
                List.of(
                        new StaticUser("fishka", "Querty67", null, null, "winfred.haag"),
                        new StaticUser("shani.marquardt", "password", null, null, "ivan")
                )
        );
    }


    @Override
    public void beforeTestExecution(ExtensionContext context) {
        final Parameter[] params = context.getRequiredTestMethod().getParameters();
        Map<UserType.Type, StaticUser> usersMap = new HashMap<>();
        Arrays.stream(params)
                .filter(parameter -> parameter.isAnnotationPresent(UserType.class))
                .map(parameter -> parameter.getAnnotation(UserType.class).value())
                .forEach(
                        userType -> {
                            StopWatch sw = StopWatch.createStarted();
                            StaticUser user = null;
                            while (user == null && sw.getTime(TimeUnit.SECONDS) < 40) {
                                Queue<StaticUser> queue = getUserQueueByType(userType);
                                user = queue.poll();
                            }

                            Allure.getLifecycle().updateTestCase(testCase ->
                                    testCase.setStart(new Date().getTime())
                            );
                            if (user == null) {
                                throw new RuntimeException("Couldn't find user during 30 seconds");
                            }
                            usersMap.put(userType, user);
                        }
                );
        context.getStore(NAMESPACE).put(context.getUniqueId(), usersMap);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void afterTestExecution(ExtensionContext context) {
        Map<Type, StaticUser> usersMap = context.getStore(NAMESPACE)
                .get(context.getUniqueId(), Map.class);

        for (Type userType : usersMap.keySet()) {
            Queue<StaticUser> queue = getUserQueueByType(userType);
            boolean isResultSuccess = queue.add(usersMap.get(userType));
            assert isResultSuccess;
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(StaticUser.class)
                && AnnotationSupport.isAnnotated(parameterContext.getParameter(), UserType.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public StaticUser resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        final Map<UserType.Type, StaticUser> userTypeStaticUserMap = extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), Map.class);
        return userTypeStaticUserMap.get(parameterContext.getParameter().getAnnotation(UserType.class).value());
    }

    private Queue<StaticUser> getUserQueueByType(Type userType) {
        return switch (userType) {
            case EMPTY -> EMPTY_USERS;
            case WITH_FRIEND -> WITH_FRIEND_USERS;
            case WITH_INCOME_REQUEST -> WITH_INCOME_REQUEST_USERS;
            case WITH_OUTCOME_REQUEST -> WITH_OUTCOME_REQUEST_USERS;
        };
    }

}
