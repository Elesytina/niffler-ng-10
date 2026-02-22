package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class EditSpendingPage extends BasePage<EditSpendingPage> {

    private final SelenideElement descriptionInput = $("#description");
    private final SelenideElement amountInput = $("#amount");
    private final SelenideElement saveBtn = $("#save");

    @Step("set new description {description}")
    public EditSpendingPage setNewSpendingDescription(String description) {
        descriptionInput.val(description);

        return this;
    }

    @Step("set sending amount {amount}")
    public EditSpendingPage editAmount(double amount) {
        amountInput.setValue(String.valueOf(amount));

        return this;
    }

    @Step("click save")
    public MainPage save() {
        saveBtn.click();

        return new MainPage();
    }
}
