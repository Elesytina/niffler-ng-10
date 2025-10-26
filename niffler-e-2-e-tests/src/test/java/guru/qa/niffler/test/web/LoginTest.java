package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.helper.TestConstantHolder.CFG;
import static guru.qa.niffler.helper.TestConstantHolder.FAKER;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

public class LoginTest {

    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin() {
        var userName = FAKER.name().username();
        var password = "password";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickCreateAccount()
                .registerUser(userName, password)
                .followToSignIn()
                .login(userName, password)
                .checkThatPageLoaded();
    }

    @Test
    void userShouldStayOnLoginPageAfterLoginWithIncorrectPassword() {
        var userName = FAKER.name().username() + randomAlphanumeric(5);
        var password = randomAlphanumeric(10);

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickCreateAccount()
                .registerUser(userName, password)
                .followToSignIn()
                .inputUserName(userName)
                .inputPassword(randomAlphanumeric(5))
                .submit()
                .checkIncorrectCredsDataError("Неверные учетные данные пользователя");
    }
}
