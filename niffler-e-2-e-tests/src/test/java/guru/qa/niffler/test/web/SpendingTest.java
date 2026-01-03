package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.SpendingCategory;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.userdata.UserJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDate;

import static com.codeborne.selenide.Selenide.open;
import static guru.qa.niffler.model.enums.CurrencyValues.RUB;
import static guru.qa.niffler.utils.RandomDataUtils.randomCurrency;
import static guru.qa.niffler.utils.RandomDataUtils.randomDouble;
import static guru.qa.niffler.utils.RandomDataUtils.randomInteger;
import static guru.qa.niffler.utils.RandomDataUtils.randomSentence;

@ExtendWith(BrowserExtension.class)
public class SpendingTest {

    private static final Config CFG = Config.getInstance();

    @User(spendings = @Spending(
            category = "Учеба",
            amount = 899,
            currency = RUB,
            description = "new description"
    ))
    @Test
    void spendingDescriptionShouldBeEditedByTableAction(UserJson userJson) {
        final String newDescription = "Обучение Niffler Next Generation";

        open(CFG.frontUrl(), LoginPage.class)
                .login(userJson.username(), userJson.testData().password())
                .searchSpending(userJson.testData().spends().getFirst().description())
                .editSpending()
                .setNewSpendingDescription(newDescription)
                .save()
                .checkThatTableContains(newDescription);
    }

    @User(categories = {@SpendingCategory})
    @Test
    void shouldCreateNewSpending(UserJson userJson) {
        var categoryName = userJson.testData().categories().getFirst().name();
        var spendingDescription = randomSentence(3);

        open(CFG.frontUrl(), LoginPage.class)
                .login(userJson.username(), userJson.testData().password())
                .clickCreateNewSpendingButton()
                .setAmount(randomInteger(10, 10000))
                .selectCurrency(randomCurrency())
                .selectCategory(categoryName)
                .selectDate(LocalDate.now())
                .setDescription(spendingDescription)
                .clickSave()
                .checkThatTableContains(spendingDescription);
    }
}
