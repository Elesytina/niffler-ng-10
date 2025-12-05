package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.SpendJson;
import guru.qa.niffler.service.spend.SpendClient;
import guru.qa.niffler.service.spend.SpendDbClient;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Date;

public class CreateSpendingExtension implements BeforeEachCallback {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CreateSpendingExtension.class);
    private final SpendClient spendClient = new SpendDbClient();

    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(
                context.getRequiredTestMethod(),
                User.class
        ).ifPresent(
                anno -> {
                    if (anno.spendings().length != 0) {
                        var annoSpending = anno.spendings()[0];
                        var username = anno.username();

                        final SpendJson created = spendClient.createSpend(
                                new SpendJson(
                                        null,
                                        new Date(),
                                        new CategoryJson(
                                                null,
                                                annoSpending.category(),
                                                username,
                                                false
                                        ),
                                        annoSpending.currency(),
                                        annoSpending.amount(),
                                        annoSpending.description(),
                                        username
                                )
                        );
                        context.getStore(NAMESPACE).put(
                                context.getUniqueId(),
                                created
                        );
                    }
                }
        );
    }
}
