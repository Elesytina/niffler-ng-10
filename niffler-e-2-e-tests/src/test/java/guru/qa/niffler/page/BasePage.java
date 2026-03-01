package guru.qa.niffler.page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;

@ParametersAreNonnullByDefault
public class BasePage<T extends BasePage<?>> {

    private final SelenideElement snackBar;

    public BasePage(SelenideDriver driver) {
        this.snackBar = driver.$(".MuiAlert-message");
    }

    public BasePage() {
        this.snackBar = Selenide.$(".MuiAlert-message");
    }

    @SuppressWarnings("unchecked")
    public T checkSnackBarText(String text) {
        snackBar.shouldHave(text(text));

        return (T) this;
    }
}
