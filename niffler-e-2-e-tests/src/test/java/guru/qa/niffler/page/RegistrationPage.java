package guru.qa.niffler.page;

import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class RegistrationPage {

    private final SelenideElement userNameInput = $("#username");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement passwordSubmitInput = $("#passwordSubmit");
    private final SelenideElement registerBtn = $("#register-button");
    private final String formErrorCssSelector = ".form__error";

    public RegistrationPage registerUser(String username, String password) {
        setUserName(username);
        setPassword(password);
        setPasswordSubmit(password);
        clickRegisterBtn();
        checkThatRegistrationIsSuccessfull();
        return this;
    }

    public RegistrationPage setUserName(String userName) {
        userNameInput.sendKeys(userName);
        return this;
    }

    public RegistrationPage setPassword(String password) {
        passwordInput.sendKeys(password);
        return this;
    }

    public RegistrationPage setPasswordSubmit(String password) {
        passwordSubmitInput.sendKeys(password);
        return this;
    }

    public RegistrationPage clickRegisterBtn() {
        registerBtn.click();
        return this;
    }

    public LoginPage followToSignIn() {
        $(Selectors.byText("Sign in")).shouldBe(visible).click();
        return page(LoginPage.class);
    }

    public RegistrationPage checkThatRegistrationIsSuccessfull() {
        $(Selectors.byText("Congratulations! You've registered!")).shouldBe(visible);
        return this;
    }

    public RegistrationPage checkUserAlreadyExistsRegistrationError(String userName) {
        var element = $(formErrorCssSelector);
        element.shouldBe(visible);
        element.shouldHave(text("Username `%s` already exists".formatted(userName)));
        return this;
    }

    public RegistrationPage checkPasswordShouldBeEqualRegistrationError() {
        var element = $(formErrorCssSelector);
        element.shouldBe(visible);
        element.shouldHave(text("Passwords should be equal"));
        return this;
    }
}
