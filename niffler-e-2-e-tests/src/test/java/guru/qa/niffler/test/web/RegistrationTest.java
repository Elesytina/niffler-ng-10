package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.userdata.UserJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.helper.TestConstantHolder.CFG;
import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

@WebTest
public class RegistrationTest {

    @Test
    void shouldRegisterNewUser() {
        var userName = randomUsername();
        var password = randomAlphanumeric(10);

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickCreateAccount()
                .setUsername(userName)
                .setPassword(password)
                .setPasswordSubmit(password)
                .clickRegisterBtn()
                .checkThatRegistrationIsSuccessful();
    }

    @Test
    @User
    void shouldNotRegisterWithExistingUser(UserJson user) {
        var userName = user.username();
        var password = user.testData().password();

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickCreateAccount()
                .setUsername(userName)
                .setPassword(password)
                .setPasswordSubmit(password)
                .clickRegisterBtn()
                .checkRegisterError("Username `%s` already exists".formatted(userName));
    }

    @Test
    void shouldShowErrorWhenPasswordAndConfirmPasswordAreNotEqual() {
        var userName = randomUsername();
        var password = randomAlphanumeric(10);
        var confirmPassword = randomAlphanumeric(9);

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickCreateAccount()
                .setUsername(userName)
                .setPassword(password)
                .setPasswordSubmit(confirmPassword)
                .clickRegisterBtn()
                .checkRegisterError("Passwords should be equal");
    }
}
