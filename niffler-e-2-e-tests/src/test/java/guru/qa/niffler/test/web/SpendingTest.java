package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.userdata.UserJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.model.enums.CurrencyValues.RUB;

@ExtendWith(BrowserExtension.class)
public class SpendingTest {

    private static final Config CFG = Config.getInstance();

    @User(spendings = @Spending(
            category = "Учеба",
            amount = 89900,
            currency = RUB,
            description = "new description"
    ))
    @Test
    void spendingDescriptionShouldBeEditedByTableAction(UserJson userJson) {
        final String newDescription = "Обучение Niffler Next Generation";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(userJson.username(),userJson.testData().password())
                .editSpending(userJson.testData().spends().getFirst().description())
                .setNewSpendingDescription(newDescription)
                .save()
                .checkThatTableContains(newDescription);
    }
}
