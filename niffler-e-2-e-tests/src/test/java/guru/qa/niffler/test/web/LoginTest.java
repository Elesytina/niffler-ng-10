package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.Test;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

public class LoginTest {

    private final Faker FAKER = new Faker();
    private static final Config CFG = Config.getInstance();

    @Test
    void mainPageShouldBeDisplayedAfterSuccessLoginPositiveTest() {
        var userName = FAKER.name().username();
        var password = "password";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickCreateAccount()
                .registerUser(userName, password)
                .followToSignIn()
                .login(userName, password)
                .checkThatPageLoaded(MainPage.class);
    }

    @Test
    void userShouldStayOnLoginPageAfterLoginWithIncorrectPasswordNegativeTest() {
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
