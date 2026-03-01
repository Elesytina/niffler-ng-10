package guru.qa.niffler.test.web;

import com.codeborne.selenide.SelenideDriver;
import guru.qa.niffler.jupiter.annotation.ScreenshotTest;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.Bubble;
import guru.qa.niffler.model.enums.CurrencyValues;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.SpendJson;
import guru.qa.niffler.model.userdata.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.utils.SelenideUtils;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.util.List;

import static guru.qa.niffler.condition.Color.green;
import static guru.qa.niffler.condition.Color.yellow;
import static guru.qa.niffler.helper.TestConstantHolder.CFG;
import static guru.qa.niffler.model.enums.CurrencyValues.RUB;
import static guru.qa.niffler.model.enums.CurrencyValues.USD;
import static guru.qa.niffler.utils.TextUtils.getExpectedStatisticArchivedItems;
import static guru.qa.niffler.utils.TextUtils.getExpectedStatisticItems;

@WebTest
public class ScreenshotStatisticsTest {

    private final SelenideDriver driver = new SelenideDriver(SelenideUtils.CHROME_CONFIG);

    @User(spendings = @Spending(category = "restaurant",
            description = "anniversary dinner",
            amount = 8999,
            currency = RUB))
    @ScreenshotTest(value = "img/statistics-component.png", rewriteExpected = true)
    void shouldDisplayStatComponent(UserJson userJson, BufferedImage expectedImage) {
        var currency = userJson.testData().spends().getFirst().currency();
        var username = userJson.username();
        var password = userJson.testData().password();
        String[] expectedTxt = getExpectedStatisticItems(userJson.testData().spends().getFirst());

        driver.open(CFG.frontUrl(), LoginPage.class)
                .login(username, password)
                .selectCurrency(currency)
                .checkExistingStatisticItems(expectedTxt)
                .statisticComponent()
                .verifyStatisticImage(expectedImage);
    }

    @User(spendings = {
            @Spending(category = "restaurant",
                    description = "anniversary dinner",
                    amount = 8990,
                    currency = USD),
            @Spending(category = "flowers",
                    description = "anniversary",
                    amount = 1899,
                    currency = USD),
    })
    @Test
    void shouldCheckStatBubbles(UserJson userJson) {
        var username = userJson.username();
        var password = userJson.testData().password();
        String[] expectedTxt = getExpectedStatisticItems(userJson.testData().spends().toArray(SpendJson[]::new));

        driver.open(CFG.frontUrl(), LoginPage.class)
                .login(username, password)
                .selectCurrency(USD)
                .checkBubbles(
                        new Bubble(yellow, expectedTxt[1]),
                        new Bubble(green, expectedTxt[0]));
    }

    @User(spendings = {
            @Spending(category = "car",
                    description = "car fixing",
                    amount = 33990,
                    currency = RUB),
            @Spending(category = "flowers",
                    description = "anniversary",
                    amount = 1899,
                    currency = RUB),
    })
    @Test
    void shouldCheckStatBubblesInAnyOrder(UserJson userJson) {
        var username = userJson.username();
        var password = userJson.testData().password();
        String[] expectedTxt = getExpectedStatisticItems(userJson.testData().spends().toArray(SpendJson[]::new));

        driver.open(CFG.frontUrl(), LoginPage.class)
                .login(username, password)
                .selectCurrency(RUB)
                .checkBubblesInAnyOrder(
                        new Bubble(green, expectedTxt[1]),
                        new Bubble(yellow, expectedTxt[0]));
    }

    @User(spendings = {
            @Spending(category = "car",
                    description = "car fixing",
                    amount = 33990,
                    currency = RUB),
            @Spending(category = "flowers",
                    description = "anniversary",
                    amount = 1899,
                    currency = RUB),
    })
    @Test
    void shouldCheckStatBubblesContain(UserJson userJson) {
        var username = userJson.username();
        var password = userJson.testData().password();
        String[] expectedTxt = getExpectedStatisticItems(userJson.testData().spends().toArray(SpendJson[]::new));

        driver.open(CFG.frontUrl(), LoginPage.class)
                .login(username, password)
                .selectCurrency(RUB)
                .checkBubblesContain(
                        new Bubble(green, expectedTxt[1]));
    }

    @User(spendings = {
            @Spending(
                    category = "Public transport",
                    description = "Metro tickets",
                    amount = 200),
            @Spending(
                    category = "Presents",
                    description = "Fruits",
                    amount = 1350)})
    @ScreenshotTest(value = "img/statistics-delete.png", rewriteExpected = true)
    void shouldDisplayCorrectStatisticsAfterDelete(UserJson user, BufferedImage expected) {
        List<SpendJson> spends = user.testData().spends();
        var currency = spends.getFirst().currency();
        String[] expectedTxt = getExpectedStatisticItems(spends.toArray(SpendJson[]::new));
        String[] updatedTxt = getExpectedStatisticItems(spends.get(1));

        driver.open(CFG.frontUrl(), LoginPage.class)
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

        driver.open(CFG.frontUrl(), LoginPage.class)
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
                    amount = 310000,
                    currency = USD
            )})
    @ScreenshotTest(value = "img/statistics-archived.png")
    void shouldDisplayArchivedCategoryStatistics(UserJson user, BufferedImage expected) {
        List<SpendJson> spends = user.testData().spends();
        SpendJson spend = spends.getFirst();
        CurrencyValues spendCurrency = spend.currency();
        CategoryJson category = spend.category();
        String expectedTxt = getExpectedStatisticArchivedItems(spends);


        driver.open(CFG.frontUrl(), LoginPage.class)
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
    @ScreenshotTest(value = "img/icon.png")
    void shouldDisplayAvatarImg(UserJson user, BufferedImage expected) {
        String path = "img/moana.png";

        driver.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .openProfilePopupMenu()
                .chooseProfile()
                .uploadNewPicture(path)
                .avatarComponent()
                .verifyAvatarImg(expected);
    }
}
