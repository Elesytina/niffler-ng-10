package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class LoginPage extends BasePage<LoginPage> {

    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement submitBtn = $("#login-button");
    private final SelenideElement createNewBtn = $("#register-button");
    private final SelenideElement formError = $(".form__error");

    @Step("login with username {username} and password {password}")
    public MainPage login(String username, String password) {
        inputUsername(username);
        inputPassword(password);
        submit();
        return page(MainPage.class);
    }

    @Step("input username {username}")
    public LoginPage inputUsername(String username) {
        usernameInput.sendKeys(username);
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
        createNewBtn.click();
        return page(RegisterPage.class);
    }

    @Step("verify error {message} when use incorrect credentials")
    public void checkIncorrectCredsDataError(String message) {
        formError.shouldBe(visible)
                .shouldHave(text(message));
    }
}