package guru.qa.niffler.test.web;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.userdata.UserJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;
import static guru.qa.niffler.helper.TestConstantHolder.CFG;
import static guru.qa.niffler.helper.TestConstantHolder.DEFAULT_PASSWORD;
import static guru.qa.niffler.page.LoginPage.LOGIN_PAGE_URL;
import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

@WebTest
public class LoginTest {

    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin() {
        var userName = randomUsername();
        var password = DEFAULT_PASSWORD;

        open(CFG.frontUrl(), LoginPage.class)
                .clickCreateAccount()
                .registerUser(userName, password)
                .followToSignIn()
                .login(userName, password)
                .checkThatPageLoaded();
    }

    @Test
    @User
    void userShouldStayOnLoginPageAfterLoginWithIncorrectPassword(UserJson user) {
        var userName = user.username();

        open(LOGIN_PAGE_URL, LoginPage.class)
                .inputUsername(userName)
                .inputPassword(randomAlphanumeric(5))
                .submit()
                .checkIncorrectCredsDataError();
    }
}
