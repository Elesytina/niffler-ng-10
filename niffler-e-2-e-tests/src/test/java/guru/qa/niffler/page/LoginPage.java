package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class LoginPage {
    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement submitBtn = $("#login-button");
    private final SelenideElement createNewBtn = $("#register-button");

    public MainPage login(String username, String password) {
        inputUserName(username);
        inputPassword(password);
        submit();
        return page(MainPage.class);
    }

    public LoginPage inputUserName(String username) {
        usernameInput.sendKeys(username);
        return this;
    }

    public LoginPage inputPassword(String password) {
        passwordInput.sendKeys(password);
        return this;
    }

    public LoginPage submit() {
        submitBtn.click();
        return this;
    }

    public RegistrationPage clickCreateAccount() {
        createNewBtn.click();
        return page(RegistrationPage.class);
    }

    public LoginPage checkIncorrectCredsDataError() {
        var element = $(".form__error");
        element.shouldBe(visible);
        element.shouldHave(text("Неверные учетные данные пользователя"));
        return this;
    }
}
