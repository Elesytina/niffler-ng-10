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

public class UsersQueueExtension implements
        BeforeTestExecutionCallback,
        AfterTestExecutionCallback,
        ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UsersQueueExtension.class);

    public record StaticUser(String username, String password, String friend, String income, String outcome) {
    }

//    private record IndexedUserType(int index, UserType userType) {
//    }

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
                                user = switch (userType) {
                                    case EMPTY -> EMPTY_USERS.poll();
                                    case WITH_FRIEND -> WITH_FRIEND_USERS.poll();
                                    case WITH_INCOME_REQUEST -> WITH_INCOME_REQUEST_USERS.poll();
                                    case WITH_OUTCOME_REQUEST -> WITH_OUTCOME_REQUEST_USERS.poll();
                                };
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
//        IntStream.range(0, params.length)
//                .filter(i ->
//                        AnnotationSupport.isAnnotated(params[i], UserType.class)
//                                && params[i].getType().isAssignableFrom(StaticUser.class))
//                .map(par ->)
//                .mapToObj(i -> new IndexedUserType(i, params[i].getAnnotation(UserType.class)))
//                .forEach(iut -> {
//                    Optional<StaticUser> user = Optional.empty();
//                    StopWatch sw = StopWatch.createStarted();
//                    while (user.isEmpty() && sw.getTime(TimeUnit.SECONDS) < 30) {
//                        user = switch (iut.userType.value()) {
//                            case EMPTY -> Optional.ofNullable(EMPTY_USERS.poll());
//                            case WITH_FRIEND -> Optional.ofNullable(WITH_FRIEND_USERS.poll());
//                            case WITH_INCOME_REQUEST -> Optional.ofNullable(WITH_INCOME_REQUEST_USERS.poll());
//                            case WITH_OUTCOME_REQUEST -> Optional.ofNullable(WITH_OUTCOME_REQUEST_USERS.poll());
//                        };
//                    }
//                    Allure.getLifecycle().updateTestCase(testCase ->
//                            testCase.setStart(new Date().getTime())
//                    );
//                    user.ifPresentOrElse(
//                            u ->
//                                    ((Map<IndexedUserType, StaticUser>) context.getStore(NAMESPACE).getOrComputeIfAbsent(
//                                            context.getUniqueId(),
//                                            key -> new HashMap<>()
//                                    )).put(iut, u),
//                            () -> {
//                                throw new IllegalStateException("Can`t obtain user after 30s.");
//                            }
//                    );
//                });
    }

    @Override
    @SuppressWarnings("unchecked")
    public void afterTestExecution(ExtensionContext context) {
        Map<Type, StaticUser> usersMap = context.getStore(NAMESPACE)
                .get(context.getUniqueId(), Map.class);

        for (Type userType : usersMap.keySet()) {
            boolean isResultSuccess = switch (userType) {
                case Type.EMPTY -> EMPTY_USERS.add(usersMap.get(userType));
                case Type.WITH_FRIEND -> WITH_FRIEND_USERS.add(usersMap.get(userType));
                case Type.WITH_INCOME_REQUEST -> WITH_INCOME_REQUEST_USERS.add(usersMap.get(userType));
                case Type.WITH_OUTCOME_REQUEST -> WITH_OUTCOME_REQUEST_USERS.add(usersMap.get(userType));
            };
            assert isResultSuccess;
        }


//        Map<IndexedUserType, StaticUser> usersFromTest = context.getStore(NAMESPACE).get(
//                context.getUniqueId(),
//                Map.class
//        );
//        if (usersFromTest != null) {
//            for (Map.Entry<IndexedUserType, StaticUser> e : usersFromTest.entrySet()) {
//                switch (e.getKey().userType.value()) {
//                    case EMPTY -> EMPTY_USERS.add(e.getValue());
//                    case WITH_FRIEND -> WITH_FRIEND_USERS.add(e.getValue());
//                    case WITH_INCOME_REQUEST -> WITH_INCOME_REQUEST_USERS.add(e.getValue());
//                    case WITH_OUTCOME_REQUEST -> WITH_OUTCOME_REQUEST_USERS.add(e.getValue());
//                }
//            }
//        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(StaticUser.class)
                && AnnotationSupport.isAnnotated(parameterContext.getParameter(), UserType.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public StaticUser resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
//        final Map<IndexedUserType, StaticUser> usersForTest = extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), Map.class);
//        return usersForTest.get(
//                new IndexedUserType(
//                        parameterContext.getIndex(),
//                        parameterContext.getParameter().getAnnotation(UserType.class)
//                )
//        );
        final Map<UserType.Type, StaticUser> userTypeStaticUserMap = extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), Map.class);
        return userTypeStaticUserMap.get(parameterContext.getParameter().getAnnotation(UserType.class).value());
    }

}
