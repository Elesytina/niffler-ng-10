package guru.qa.niffler.page;

import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class RegisterPage {

    private final SelenideElement userNameInput = $("#username");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement passwordSubmitInput = $("#passwordSubmit");
    private final SelenideElement registerBtn = $("#register-button");
    private final String formErrorCssSelector = ".form__error";

    public RegisterPage registerUser(String username, String password) {
        setUserName(username);
        setPassword(password);
        setPasswordSubmit(password);
        clickRegisterBtn();
        checkThatRegistrationIsSuccessfull();
        return this;
    }

    public RegisterPage setUserName(String userName) {
        userNameInput.sendKeys(userName);
        return this;
    }

    public RegisterPage setPassword(String password) {
        passwordInput.sendKeys(password);
        return this;
    }

    public RegisterPage setPasswordSubmit(String password) {
        passwordSubmitInput.sendKeys(password);
        return this;
    }

    public RegisterPage clickRegisterBtn() {
        registerBtn.click();
        return this;
    }

    public LoginPage followToSignIn() {
        $(Selectors.byText("Sign in")).shouldBe(visible).click();
        return page(LoginPage.class);
    }

    public void checkThatRegistrationIsSuccessfull() {
        $(Selectors.byText("Congratulations! You've registered!")).shouldBe(visible);
    }

    public RegisterPage checkUserAlreadyExistsRegistrationError(String userName) {
        var element = $(formErrorCssSelector);
        element.shouldBe(visible);
        element.shouldHave(text("Username `%s` already exists".formatted(userName)));
        return this;
    }

    public RegisterPage checkPasswordShouldBeEqualRegistrationError() {
        var element = $(formErrorCssSelector);
        element.shouldBe(visible);
        element.shouldHave(text("Passwords should be equal"));
        return this;
    }
}
