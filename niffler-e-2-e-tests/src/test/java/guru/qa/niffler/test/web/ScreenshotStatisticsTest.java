package guru.qa.niffler.test.web;

import guru.qa.niffler.jupiter.annotation.ScreenshotTest;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.SpendingCategory;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.enums.CurrencyValues;
import guru.qa.niffler.model.spend.SpendJson;
import guru.qa.niffler.model.userdata.UserJson;
import guru.qa.niffler.page.LoginPage;

import java.awt.image.BufferedImage;
import java.util.List;

import static com.codeborne.selenide.Selenide.open;
import static guru.qa.niffler.helper.TestConstantHolder.CFG;
import static guru.qa.niffler.model.enums.CurrencyValues.RUB;
import static guru.qa.niffler.model.enums.CurrencyValues.USD;

@WebTest
public class ScreenshotStatisticsTest {

    @User(spendings = @Spending(category = "restaurant",
            description = "dinner",
            amount = 899,
            currency = RUB))
    @ScreenshotTest(value = "img/statistics-component.png", rewriteExpected = true)
    void checkStatComponent(UserJson userJson, BufferedImage expectedImage) {
        var currency = userJson.testData().spends().getFirst().currency();
        var username = userJson.username();
        var password = userJson.testData().password();

        open(CFG.frontUrl(), LoginPage.class)
                .login(username, password)
                .selectCurrency(currency)
                .checkStatisticItems("restaurant 899 ₽")
                .statisticComponent()
                .verifyStatisticImage(expectedImage);
    }

    @User(
            spendings = {
                    @Spending(
                            category = "Public transport",
                            description = "Metro tickets",
                            amount = 2000
                    ),
                    @Spending(
                            category = "Presents",
                            description = "Fruits",
                            amount = 1350
                    )
            }
    )
    @ScreenshotTest(value = "img/statistics-archived.png", rewriteExpected = true)
    void shouldDisplayArchivedCategories(UserJson user, BufferedImage expected) {
        open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .checkStatisticItems("Поездки 9500 ₽", "Archived 3100 ₽")
                .statisticComponent()
                .verifyStatisticImage(expected);
    }

    @User(spendings = {
            @Spending(
                    category = "trip",
                    description = "holiday trip",
                    amount = 190000,
                    currency = USD
            ),
            @Spending(
                    category = "clothes",
                    description = "jacket",
                    amount = 11000,
                    currency = USD
            )})
    @ScreenshotTest(value = "img/statistics-delete.png", rewriteExpected = true)
    void shouldDisplayStatisticsWhenDeleteSpend(UserJson user, BufferedImage expected) {
        List<SpendJson> spends = user.testData().spends();
        String username = user.username();
        String password = user.testData().password();
        SpendJson spend1 = spends.getFirst();
        CurrencyValues currency = spend1.currency();
        String description = spend1.description();

        open(CFG.frontUrl(), LoginPage.class)
                .login(username, password)
                .selectCurrency(currency)
                .checkStatisticItems("trip 190000 $", "clothes 11000 $")
                .deleteSpending(description)
                .statisticComponent()
                .verifyStatisticImage(expected);
    }
}
