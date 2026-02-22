package guru.qa.niffler.page.component;

import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class SubmitModal extends BaseComponent<SubmitModal> {

    public SubmitModal() {
        super($(byAttribute("role", "dialog")));
    }

    public void submit(String text) {
        self.$(byText(text)).click();
    }
}
