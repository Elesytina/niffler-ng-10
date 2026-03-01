package guru.qa.niffler.test.web;

import com.codeborne.selenide.SelenideDriver;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.userdata.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.utils.SelenideUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static guru.qa.niffler.helper.TestConstantHolder.CFG;
import static guru.qa.niffler.helper.TestConstantHolder.DEFAULT_PASSWORD;
import static guru.qa.niffler.page.LoginPage.LOGIN_PAGE_URL;
import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

public class LoginTest {

    private final SelenideDriver driver = new SelenideDriver(SelenideUtils.CHROME_CONFIG);

    @RegisterExtension
    private final BrowserExtension browserExtension = new BrowserExtension();

    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin() {
        var userName = randomUsername();
        var password = DEFAULT_PASSWORD;

        driver.open(CFG.frontUrl(), LoginPage.class)
                .clickCreateAccount()
                .registerUser(userName, password)
                .followToSignIn()
                .login(userName, password)
                .checkThatPageLoaded();
    }

    @Test
    @User
    void userShouldStayOnLoginPageAfterLoginWithIncorrectPassword(UserJson user) {
        browserExtension.getDrivers().add(driver);
        var userName = user.username();

        driver.open(LOGIN_PAGE_URL);
        new LoginPage(driver)
                .inputUsername(userName)
                .inputPassword(randomAlphanumeric(5))
                .submit()
                .checkIncorrectCredsDataError("Неверные учетные данные пользователя");
    }
}
