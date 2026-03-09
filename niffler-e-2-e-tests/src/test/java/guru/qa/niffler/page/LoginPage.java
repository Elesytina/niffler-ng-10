package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;
import static guru.qa.niffler.helper.TestConstantHolder.CFG;

public class LoginPage extends BasePage<LoginPage> {

    public static final String LOGIN_PAGE_URL = CFG.frontUrl() + "login";

    private final SelenideElement usernameInput;
    private final SelenideElement passwordInput;
    private final SelenideElement submitBtn;
    private final SelenideElement createNewBtn;
    private final SelenideElement formError;

    public LoginPage() {
        this.usernameInput = $("#username");
        this.passwordInput = $("#password");
        this.submitBtn = $("#login-button");
        this.createNewBtn = $("#register-button");
        this.formError = $(".form__error");
    }

    public LoginPage(SelenideDriver driver) {
        super(driver);
        this.usernameInput = driver.$("#username");
        this.passwordInput = driver.$("#password");
        this.submitBtn = driver.$("#login-button");
        this.createNewBtn = driver.$("#register-button");
        this.formError = driver.$(".form__error");
    }

    @Step("login with username {username} and password {password}")
    public MainPage login(String username, String password) {
        inputUsername(username);
        inputPassword(password);
        submit();
        return page(MainPage.class);
    }

    @Step("input username {username}")
    public LoginPage inputUsername(String username) {
        usernameInput.shouldBe(visible, Duration.ofSeconds(8)).sendKeys(username);
        return this;
    }

    @Step("input password {password}")
    public LoginPage inputPassword(String password) {
        passwordInput.sendKeys(password);
        return this;
    }

    @Step("submit action")
    public LoginPage submit() {
        submitBtn.click();
        return this;
    }

    @Step("click create new account")
    public RegisterPage clickCreateAccount() {
        createNewBtn.shouldBe(visible, Duration.ofSeconds(8)).click();
        return page(RegisterPage.class);
    }

    @Step("verify error {message} when use incorrect credentials")
    public void checkIncorrectCredsDataError() {
        SelenideElement form = formError.shouldBe(visible);
        String text = form.innerText();
        Assertions.assertTrue(text.contains("Неверные учетные данные пользователя") || text.contains("Bad credentials"));
    }
}