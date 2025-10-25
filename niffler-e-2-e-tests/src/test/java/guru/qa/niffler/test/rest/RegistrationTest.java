package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.test.TestConstantHolder.FAKER;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

@WebTest
public class RegistrationTest {

    private static final Config CFG = Config.getInstance();

    @Test
    void shouldRegisterNewUserPositive() {
        var userName = FAKER.name().username();
        var password = randomAlphanumeric(10);

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickCreateAccount()
                .setUserName(userName)
                .setPassword(password)
                .setPasswordSubmit(password)
                .clickRegisterBtn()
                .checkThatRegistrationIsSuccessfull();
    }

    @Test
    void shouldNotRegisterWithExistingUserNegative() {
        var userName = FAKER.name().username();
        var password = randomAlphanumeric(10);

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickCreateAccount()
                .registerUser(userName, password)
                .followToSignIn()
                .clickCreateAccount()
                .setUserName(userName)
                .setPassword(password)
                .setPasswordSubmit(password)
                .clickRegisterBtn()
                .checkUserAlreadyExistsRegistrationError(userName);
    }

    @Test
    void shouldShowErrorWhenPasswordAndConfirmPasswordAreNotEqualNegative() {
        var userName = FAKER.name().username();
        var password = randomAlphanumeric(10);
        var confirmPassword = randomAlphanumeric(10);

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickCreateAccount()
                .setUserName(userName)
                .setPassword(password)
                .setPasswordSubmit(confirmPassword)
                .clickRegisterBtn()
                .checkPasswordShouldBeEqualRegistrationError();
    }
}
