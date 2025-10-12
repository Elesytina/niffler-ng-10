package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.test.helpers.TestHelper.FAKER;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

@WebTest
public class LoginTest {

    private static final Config CFG = Config.getInstance();

    @Test
    void test_mainPageShouldBeDisplayedAfterSuccessLogin_positive() {
        var userName = FAKER.name().username();
        var password = randomAlphanumeric(10);

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickCreateAccount()
                .registerUser(userName, password)
                .followToSignIn()
                .login(userName, password)
                .checkThatPageLoaded();
    }


    @Test
    public void test_userShouldStayOnLoginPageAfterLoginWithIncorrectPassword_negative() {
        var userName = FAKER.name().username() + randomAlphanumeric(5);
        var password = randomAlphanumeric(10);

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickCreateAccount()
                .registerUser(userName, password)
                .followToSignIn()
                .inputUserName(userName)
                .inputPassword(randomAlphanumeric(5))
                .submit()
                .checkIncorrectCredsDataError();
    }
}
