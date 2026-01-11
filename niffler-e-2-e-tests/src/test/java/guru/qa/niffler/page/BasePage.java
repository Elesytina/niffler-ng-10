package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class BasePage<T extends BasePage<?>> {

    private final SelenideElement snackBar = $(".MuiAlert-message");

    @SuppressWarnings("unchecked")
    public T checkSnackBarText(String text) {
        snackBar.shouldHave(text(text));

        return (T) this;
    }
}
