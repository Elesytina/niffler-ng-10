package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.helper.TestConstantHolder.CFG;
import static guru.qa.niffler.helper.TestConstantHolder.DEFAULT_PASSWORD;
import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

public class LoginTest {

    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin() {
        var userName = randomUsername();
        var password = DEFAULT_PASSWORD;

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickCreateAccount()
                .registerUser(userName, password)
                .followToSignIn()
                .login(userName, password)
                .checkThatPageLoaded();
    }

    @Test
    void userShouldStayOnLoginPageAfterLoginWithIncorrectPassword() {
        var userName = randomUsername();
        var password = randomAlphanumeric(10);

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickCreateAccount()
                .registerUser(userName, password)
                .followToSignIn()
                .inputUsername(userName)
                .inputPassword(randomAlphanumeric(5))
                .submit()
                .checkIncorrectCredsDataError("Неверные учетные данные пользователя");
    }
}
