package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.SpendingCategory;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.page.LoginPage;
import io.qameta.allure.Description;
import org.junit.jupiter.api.Test;

@WebTest
public class SpendingCategoryTest {

    private static final Config CFG = Config.getInstance();

    private final String username = "fishka";

    @SpendingCategory(
            username = username,
            archived = false)
    @Test
    @Description("active Category Should Be Present In Profile")
    void activeCategoryShouldBePresentInProfilePositiveTest() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(username, "Querty67")
                .clickProfileIcon()
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
    void archivedCategoryShouldBePresentInProfilePositiveTest() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(username, "Querty67")
                .checkThatActiveCategoryPresent();

    }

}

