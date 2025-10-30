package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.SpendingCategory;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.page.LoginPage;
import io.qameta.allure.Description;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.helper.TestConstantHolder.CFG;

@WebTest
public class SpendingCategoryTest {

    private final String username = "fishka";

    @SpendingCategory(
            username = username)
    @Test
    @Description("active Category Should Be Present In Profile")
    void archivedCategoryShouldBePresentInProfilePositiveTest() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(username, "Querty67")
                .openProfilePopupMenu()
                .chooseProfile()
                .checkThatPageIsDisplayed()
                .showArchived()
                .checkThatArchivedCategoriesExist();
    }

    @SpendingCategory(
            username = username,
            archived = true)
    @Test
    @Description("archived Category Should Be Present In Profile")
    void activeCategoryShouldBePresentInProfilePositiveTest() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(username, "Querty67")
                .checkThatActiveCategoryPresent();
    }

}

