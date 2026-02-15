package guru.qa.niffler.test.web;

import guru.qa.niffler.jupiter.annotation.ScreenshotTest;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.enums.CurrencyValues;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.SpendJson;
import guru.qa.niffler.model.userdata.UserJson;
import guru.qa.niffler.page.LoginPage;

import java.awt.image.BufferedImage;
import java.util.List;

import static com.codeborne.selenide.Selenide.open;
import static guru.qa.niffler.helper.TestConstantHolder.CFG;
import static guru.qa.niffler.model.enums.CurrencyValues.RUB;
import static guru.qa.niffler.model.enums.CurrencyValues.USD;
import static guru.qa.niffler.page.MainPage.getExpectedStatisticArchivedItems;
import static guru.qa.niffler.page.MainPage.getExpectedStatisticItems;

@WebTest
public class ScreenshotStatisticsTest {

    @User(spendings = @Spending(category = "restaurant",
            description = "dinner",
            amount = 899,
            currency = RUB))
    @ScreenshotTest(value = "img/statistics-component.png")
    void shouldDisplayStatComponent(UserJson userJson, BufferedImage expectedImage) {
        var currency = userJson.testData().spends().getFirst().currency();
        var username = userJson.username();
        var password = userJson.testData().password();
        String[] expectedTxt = getExpectedStatisticItems(userJson.testData().spends().getFirst());

        open(CFG.frontUrl(), LoginPage.class)
                .login(username, password)
                .selectCurrency(currency)
                .checkExistingStatisticItems(expectedTxt)
                .statisticComponent()
                .verifyStatisticImage(expectedImage);
    }

    @User(spendings = {
            @Spending(
                    category = "Public transport",
                    description = "Metro tickets",
                    amount = 2000),
            @Spending(
                    category = "Presents",
                    description = "Fruits",
                    amount = 1350)})
    @ScreenshotTest(value = "img/statistics-archived.png", rewriteExpected = true)
    void shouldDisplayCorrectStatisticsAfterDelete(UserJson user, BufferedImage expected) {
        List<SpendJson> spends = user.testData().spends();
        var currency = spends.getFirst().currency();
        String[] expectedTxt = getExpectedStatisticItems(spends.toArray(SpendJson[]::new));
        String[] updatedTxt = getExpectedStatisticItems(spends.get(1));

        open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .selectCurrency(currency)
                .checkExistingStatisticItems(expectedTxt)
                .deleteSpending(spends.getFirst().description())
                .checkExistingStatisticItems(updatedTxt)
                .statisticComponent()
                .verifyStatisticImage(expected);
    }

    @User(spendings = {
            @Spending(
                    category = "clothes",
                    description = "jacket",
                    amount = 11000
            )})
    @ScreenshotTest(value = "img/statistics-edited.png")
    void shouldDisplayStatisticsAfterEditSpend(UserJson user, BufferedImage expected) {
        List<SpendJson> spends = user.testData().spends();
        SpendJson spend = spends.getFirst();
        CurrencyValues spendCurrency = spend.currency();

        open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .selectCurrency(spendCurrency)
                .editSpending(spend.description())
                .editAmount(12000)
                .save()
                .selectCurrency(spendCurrency)
                .statisticComponent()
                .verifyStatisticImage(expected);
    }

    @User(spendings = {
            @Spending(
                    category = "car",
                    description = "new car",
                    amount = 110000,
                    currency = USD
            )})
    @ScreenshotTest(value = "img/statistics-archived.png")
    void shouldDisplayArchivedCategoryStatistics(UserJson user, BufferedImage expected) {
        List<SpendJson> spends = user.testData().spends();
        SpendJson spend = spends.getFirst();
        CurrencyValues spendCurrency = spend.currency();
        CategoryJson category = spend.category();
        String expectedTxt = getExpectedStatisticArchivedItems(spends);


        open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .openProfilePopupMenu()
                .chooseProfile()
                .archiveCategory(category.name())
                .toMainPage()
                .selectCurrency(spendCurrency)
                .checkExistingStatisticItems(expectedTxt)
                .statisticComponent()
                .verifyStatisticImage(expected);
    }

    @User
    @ScreenshotTest(value = "img/ava.png")
    void shouldDisplayAvatarImg(UserJson user, BufferedImage expected) {
        String path = "D:\\Courses\\niffler-at-courses\\niffler-e-2-e-tests\\src\\test\\resources\\img\\ava.png";

        open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .openProfilePopupMenu()
                .chooseProfile()
                .uploadNewPicture(path)
                .avatarComponent()
                .verifyAvatarImg(expected);
    }
}
