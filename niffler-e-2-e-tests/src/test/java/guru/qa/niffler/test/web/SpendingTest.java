package guru.qa.niffler.test.web;

import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.SpendingCategory;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.userdata.UserJson;
import guru.qa.niffler.page.LoginPage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static com.codeborne.selenide.Selenide.open;
import static guru.qa.niffler.helper.TestConstantHolder.CFG;
import static guru.qa.niffler.model.enums.CurrencyValues.RUB;
import static guru.qa.niffler.utils.RandomDataUtils.randomCategoryName;
import static guru.qa.niffler.utils.RandomDataUtils.randomCurrency;
import static guru.qa.niffler.utils.RandomDataUtils.randomInteger;
import static guru.qa.niffler.utils.RandomDataUtils.randomName;
import static guru.qa.niffler.utils.RandomDataUtils.randomSentence;

@Slf4j
@WebTest
public class SpendingTest {

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
                .checkSnackBarText("Spending is edited successfully")
                .spendingTable()
                .checkTableContains(newDescription);
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
                .checkSnackBarText("New spending is successfully created")
                .spendingTable()
                .checkTableContains(spendingDescription);
    }

    @User
    @Test
    void shouldEditProfile(UserJson userJson) {
        open(CFG.frontUrl(), LoginPage.class)
                .login(userJson.username(), userJson.testData().password())
                .openProfilePopupMenu()
                .chooseProfile()
                .checkThatPageIsDisplayed()
                .editName(randomName())
                .addCategory(randomCategoryName())
                .save()
                .checkSnackBarText("Profile successfully updated")
                .checkThatProfileUpdated();
    }

}
