package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class RegisterPage {
    private final SelenideElement userNameInput = $("#username");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement passwordSubmitInput = $("#passwordSubmit");
    private final SelenideElement registerBtn = $("#register-button");
    private final SelenideElement formError = $(".form__error");

    @Step("register customer with username {username} and password {password}")
    public RegisterPage registerUser(String username, String password) {
        setUsername(username);
        setPassword(password);
        setPasswordSubmit(password);
        clickRegisterBtn();
        checkThatRegistrationIsSuccessful();
        return this;
    }

    @Step("set username {username}")
    public RegisterPage setUsername(String userName) {
        userNameInput.sendKeys(userName);
        return this;
    }

    @Step("set password {password}")
    public RegisterPage setPassword(String password) {
        passwordInput.sendKeys(password);
        return this;
    }

    @Step("submit password {password}")
    public RegisterPage setPasswordSubmit(String password) {
        passwordSubmitInput.sendKeys(password);
        return this;
    }

    @Step("click resister")
    public RegisterPage clickRegisterBtn() {
        registerBtn.click();
        return this;
    }

    @Step("follow to sign in")
    public LoginPage followToSignIn() {
        $(byText("Sign in")).shouldBe(visible).click();
        return page(LoginPage.class);
    }

    @Step("verify that element with text 'Congratulations! You've registered!' is present")
    public void checkThatRegistrationIsSuccessful() {
        $(byText("Congratulations! You've registered!")).shouldBe(visible);
    }

    @Step("verify error with message {errorMessage}")
    public void checkRegisterError(String errorMessage) {
        formError.shouldBe(visible)
                .shouldHave(text(errorMessage));
    }
}
