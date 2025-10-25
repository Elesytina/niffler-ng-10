package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.SpendingCategory;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.service.category.CategoryApiClient;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import static guru.qa.niffler.test.TestConstantHolder.FAKER;


public class CategoryExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);

    private final CategoryApiClient categoryClient = new CategoryApiClient();


    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(
                context.getRequiredTestMethod(),
                SpendingCategory.class
        ).ifPresent(
                anno -> {
                    var newCategoryName = FAKER.food().dish();

                    var categoryJson = new CategoryJson(
                            null,
                            newCategoryName,
                            anno.username(),
                            false);

                    CategoryJson created = categoryClient.createCategory(categoryJson);

                    if (anno.archived()) {
                        var archivedCategoryJson = new CategoryJson(
                                created.id(),
                                created.name(),
                                anno.username(),
                                true);

                        created = categoryClient.updateCategory(archivedCategoryJson);
                    }

                    context.getStore(NAMESPACE).put(context.getUniqueId(), created);
                }
        );
    }

    @Override
    public void afterEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(
                context.getRequiredTestMethod(),
                SpendingCategory.class
        ).ifPresent(
                anno -> {
                    if (!anno.archived()) {
                        var categoryJson = context.getStore(NAMESPACE).get(context.getUniqueId(), CategoryJson.class);

                        var categoryJsonForUpdate = new CategoryJson(
                                categoryJson.id(),
                                categoryJson.name(),
                                categoryJson.username(),
                                true);

                        categoryClient.updateCategory(categoryJsonForUpdate);
                    }
                }
        );
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson.class);
    }

    @Override
    public CategoryJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), CategoryJson.class);
    }
}
