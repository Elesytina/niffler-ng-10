package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class RegisterPage extends BasePage<RegisterPage> {

    private final SelenideElement userNameInput;
    private final SelenideElement passwordInput;
    private final SelenideElement passwordSubmitInput;
    private final SelenideElement registerBtn;
    private final SelenideElement formError;

    public RegisterPage() {
        this.userNameInput = $("#username");
        this.passwordInput = $("#password");
        this.passwordSubmitInput = $("#passwordSubmit");
        this.registerBtn = $("#register-button");
        this.formError = $(".form__error");
    }

    public RegisterPage(SelenideDriver driver) {
        super(driver);
        this.userNameInput = driver.$("#username");
        this.passwordInput = driver.$("#password");
        this.passwordSubmitInput = driver.$("#passwordSubmit");
        this.registerBtn = driver.$("#register-button");
        this.formError = driver.$(".form__error");
    }

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
