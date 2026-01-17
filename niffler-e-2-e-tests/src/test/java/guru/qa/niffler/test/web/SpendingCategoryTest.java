package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.SpendingCategory;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.userdata.UserJson;
import guru.qa.niffler.page.LoginPage;
import io.qameta.allure.Description;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.helper.TestConstantHolder.CFG;

@WebTest
public class SpendingCategoryTest {

    @User(categories = @SpendingCategory(
            archived = true))
    @Test
    @Description("active Category Should Be Present In Profile")
    void archivedCategoryShouldBePresentInProfilePositiveTest(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .openProfilePopupMenu()
                .chooseProfile()
                .checkThatPageIsDisplayed()
                .showArchived()
                .checkThatArchivedCategoriesExist();
    }

    @User(categories = @SpendingCategory)
    @Test
    @Description("archived Category Should Be Present In Profile")
    void activeCategoryShouldBePresentInProfilePositiveTest(UserJson user) {
        var username = user.username();
        var password = user.testData().password();
        var activeCategory = user.testData().categories()
                .stream()
                .filter(c -> !c.archived()).findAny();
        Assertions.assertTrue(activeCategory.isPresent(), "Active Category should not be present");

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(username, password)
                .openProfilePopupMenu()
                .chooseProfile()
                .checkThatPageIsDisplayed()
                .checkThatActiveCategoryPresent(activeCategory.get().name());
    }

}

