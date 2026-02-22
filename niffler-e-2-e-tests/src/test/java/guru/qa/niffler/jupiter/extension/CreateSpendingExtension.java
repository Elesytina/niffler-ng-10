package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.SpendJson;
import guru.qa.niffler.model.userdata.UserJson;
import guru.qa.niffler.service.spend.SpendClient;
import guru.qa.niffler.service.spend.SpendDbClient;
import org.apache.commons.lang.ArrayUtils;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static guru.qa.niffler.jupiter.extension.TestMethodContextExtension.context;

public class CreateSpendingExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CreateSpendingExtension.class);
    private final SpendClient spendClient = new SpendDbClient();

    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(userAnno -> {
                            if (ArrayUtils.isNotEmpty(userAnno.spendings())) {

                                Optional<UserJson> testUser = UserExtension.createdUser();
                                final String username = testUser.isPresent()
                                        ? testUser.get().username()
                                        : userAnno.username();

                                List<SpendJson> result = new ArrayList<>();

                                for (Spending spendAnno : userAnno.spendings()) {
                                    SpendJson spendJson = new SpendJson(
                                            null,
                                            new Date(),
                                            new CategoryJson(
                                                    null,
                                                    spendAnno.category(),
                                                    username,
                                                    false
                                            ),
                                            spendAnno.currency(),
                                            spendAnno.amount(),
                                            spendAnno.description(),
                                            username
                                    );

                                    SpendJson created = spendClient.addSpend(spendJson);
                                    result.add(created);
                                }

                                if (testUser.isPresent()) {
                                    testUser.get().testData().spends().addAll(
                                            result
                                    );
                                } else {
                                    context.getStore(NAMESPACE).put(
                                            context.getUniqueId(),
                                            result.toArray(SpendJson[]::new)
                                    );
                                }
                            }
                        }
                );
    }


    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws
            ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(SpendJson[].class);
    }

    @Override
    public SpendJson[] resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws
            ParameterResolutionException {
        return createdSpending();
    }

    public static SpendJson[] createdSpending() {
        final ExtensionContext methodContext = context();
        return methodContext.getStore(NAMESPACE)
                .get(methodContext.getUniqueId(), SpendJson[].class);
    }
}
