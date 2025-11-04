package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.service.category.CategoryClient;
import guru.qa.niffler.service.category.CategoryDbClient;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;


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
                        var annoCategory = anno.categories()[0];
                        var username = anno.username();

                        var newCategoryName = RandomDataUtils.randomCategoryName();

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

                            created = categoryClient.updateCategory(archivedCategoryJson);
                        }

                        context.getStore(NAMESPACE).put(context.getUniqueId(), created);

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
                    if (anno.categories().length != 0) {
                        var categoryAnno = anno.categories()[0];
                        var username = anno.username();

                        if (categoryAnno.archived()) {
                            var categoryJson = context.getStore(NAMESPACE).get(context.getUniqueId(), CategoryJson.class);

                            var categoryJsonForUpdate = new CategoryJson(
                                    categoryJson.id(),
                                    categoryJson.name(),
                                    username,
                                    true);

                            categoryClient.updateCategory(categoryJsonForUpdate);
                        }
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
