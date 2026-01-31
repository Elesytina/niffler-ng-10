package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.SpendingCategory;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.userdata.UserJson;
import guru.qa.niffler.service.category.CategoryClient;
import guru.qa.niffler.service.category.CategoryDbClient;
import org.junit.jupiter.api.extension.AfterEachCallback;
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
import static guru.qa.niffler.utils.RandomDataUtils.randomCategoryName;


public class CategoryExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);

    private final CategoryClient categoryClient = new CategoryDbClient();

    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(
                context.getRequiredTestMethod(),
                User.class
        ).ifPresent(
                anno -> {
                    if (anno.categories().length != 0) {
                        Optional<UserJson> testUser = UserExtension.createdUser();

                        var username = testUser.isPresent()
                                ? testUser.get().username()
                                : anno.username();

                        List<CategoryJson> categories = new ArrayList<>();
                        for (SpendingCategory annoCategory : anno.categories()) {
                            var newCategoryName = annoCategory.name().isEmpty()
                                    ? randomCategoryName()
                                    : annoCategory.name();

                            var categoryJson = new CategoryJson(
                                    null,
                                    newCategoryName,
                                    username,
                                    false);

                            CategoryJson created = categoryClient.createCategory(categoryJson);

                            if (annoCategory.archived()) {
                                var archivedCategoryJson = new CategoryJson(
                                        created.id(),
                                        created.name(),
                                        username,
                                        true);

                                CategoryJson updated = categoryClient.updateCategory(archivedCategoryJson);
                                categories.add(updated);
                            } else {
                                categories.add(created);
                            }

                            if (testUser.isPresent()) {
                                testUser.get().testData().categories().addAll(categories);
                            } else {
                                context.getStore(NAMESPACE).put(
                                        context.getUniqueId(),
                                        categories.toArray(CategoryJson[]::new));
                            }
                        }
                    }
                }
        );
    }

    @Override
    public void afterEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(
                context.getRequiredTestMethod(),
                User.class
        ).ifPresent(
                anno -> {
                    if (createdCategories() != null) {

                        for (CategoryJson category : createdCategories()) {

                            if (!category.archived()) {
                                var categoryJsonForUpdate = new CategoryJson(
                                        category.id(),
                                        category.name(),
                                        category.username(),
                                        true);

                                categoryClient.updateCategory(categoryJsonForUpdate);
                            }
                        }
                    }
                }
        );
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson[].class);
    }

    @Override
    public CategoryJson[] resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return createdCategories();
    }

    public static CategoryJson[] createdCategories() {
        final ExtensionContext methodContext = context();
        return methodContext.getStore(NAMESPACE)
                .get(methodContext.getUniqueId(), CategoryJson[].class);
    }
}
