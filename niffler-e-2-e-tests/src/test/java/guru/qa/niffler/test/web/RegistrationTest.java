package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

@WebTest
public class RegistrationTest {

    private static final Config CFG = Config.getInstance();
    private static final Faker FAKER = Faker.instance();

    @Test
    public void test_shouldRegisterNewUser_positive() {
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
    public void test_shouldNotRegisterWithExistingUser_negative() {
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
}
