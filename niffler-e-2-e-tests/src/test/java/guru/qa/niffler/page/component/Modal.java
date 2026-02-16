package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class Modal extends BaseComponent<Modal> {

    private final SelenideElement declineBtn = self.$(byText("Decline"));
    private final SelenideElement closeBtn = self.$(byText("Close"));


    public Modal() {
        super($(byAttribute("role", "dialog")));
    }

    public void close() {
        closeBtn.click();
    }

    public void decline() {
        declineBtn.click();
    }
}