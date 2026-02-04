package guru.qa.niffler.test.web;

import guru.qa.niffler.jupiter.annotation.ScreenshotTest;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.SpendingCategory;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.enums.CurrencyValues;
import guru.qa.niffler.model.userdata.UserJson;
import guru.qa.niffler.page.LoginPage;

import java.awt.image.BufferedImage;
import java.io.IOException;

import static com.codeborne.selenide.Selenide.open;
import static guru.qa.niffler.helper.TestConstantHolder.CFG;


public class ScreenshotStatisticsTest {

    @User(spendings = @Spending(category = "restaurant",
            description = "dinner at a restaurant",
            amount = 899.00,
            currency = CurrencyValues.RUB))
    @ScreenshotTest(value = "img/stat.png")
    void checkStatComponent(UserJson userJson, BufferedImage expectedImage) {
        var currency = userJson.testData().spends().getFirst().currency();
        var username = userJson.username();
        var password = userJson.testData().password();
        var category = userJson.testData().spends().getFirst().category().name();
        var amount = userJson.testData().spends().getFirst().amount();

        open(CFG.frontUrl(), LoginPage.class)
                .login(username, password)
                .selectCurrency(currency)
                .statisticComponent()
                .checkStatisticItems(category, amount.toString())
                .verifyStatisticImage(expectedImage);
             //
    }

    @User(
            spendings = @Spending(
                    category = "Обучение",
                    description = "Обучение Advanced 2.0",
                    amount = 79990
            )
    )
    @ScreenshotTest(value = "img/expected-stat.png", rewriteExpected = true)
    void checkStatComponentWithRewrite(UserJson user, BufferedImage expected) {
        open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .statisticComponent()
                .checkStatisticItems("Обучение 79990 ₽")
                .verifyStatisticImage(expected);
    }

    @User(
            categories = {
                    @SpendingCategory(name = "Поездки"),
                    @SpendingCategory(name = "Ремонт", archived = true),
                    @SpendingCategory(name = "Страховка", archived = true)
            },
            spendings = {
                    @Spending(
                            category = "Поездки",
                            description = "В Москву",
                            amount = 9500
                    ),
                    @Spending(
                            category = "Ремонт",
                            description = "Цемент",
                            amount = 100
                    ),
                    @Spending(
                            category = "Страховка",
                            description = "ОСАГО",
                            amount = 3000
                    )
            }
    )
    @ScreenshotTest(value = "img/expected-stat-archived.png", rewriteExpected = true)
    void statComponentShouldDisplayArchivedCategories(UserJson user, BufferedImage expected) throws IOException {
        open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .statisticComponent()
                .checkStatisticItems("Поездки 9500 ₽", "Archived 3100 ₽")
                .verifyStatisticImage(expected);
    }
}
