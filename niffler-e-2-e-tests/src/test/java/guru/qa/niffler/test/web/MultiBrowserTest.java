package guru.qa.niffler.test.web;

import com.codeborne.selenide.SelenideDriver;
import guru.qa.niffler.jupiter.BrowserConverter;
import guru.qa.niffler.jupiter.extension.NonStaticBrowserExtension;
import guru.qa.niffler.model.enums.Browser;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.EnumSource;

import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

public class MultiBrowserTest {

    @RegisterExtension
    NonStaticBrowserExtension nonStaticBrowserExtension = new NonStaticBrowserExtension();

    @ParameterizedTest
    @Execution(ExecutionMode.CONCURRENT)
    @EnumSource(value = Browser.class)
    void mainPageShouldBeDisplayedAfterSuccessLogin(@ConvertWith(BrowserConverter.class) SelenideDriver driver) {
        NonStaticBrowserExtension.drivers().add(driver);
        var userName = randomUsername();

        driver.open(LoginPage.LOGIN_PAGE_URL);
        new LoginPage(driver)
                .inputUsername(userName)
                .inputPassword(randomAlphanumeric(5))
                .submit()
                .checkIncorrectCredsDataError();
    }
}
