package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.enums.CurrencyValues;
import guru.qa.niffler.page.component.Calendar;

import java.time.LocalDate;

import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class AddNewPendingPage {

    private final SelenideElement amountInput = $("#amount");
    private final SelenideElement currencySelect = $("#currency");
    private final SelenideElement descriptionInput = $("#description");
    private final SelenideElement saveBtn = $("#save");
    private final Calendar calendar = new Calendar();

    public AddNewPendingPage setAmount(int amount) {
        amountInput.sendKeys(String.valueOf(amount));

        return this;
    }

    public AddNewPendingPage selectCurrency(CurrencyValues currency) {
        currencySelect.click();
        $(byAttribute("data-value", currency.toString()))
                .click();

        return this;
    }

    public AddNewPendingPage selectCategory(String categoryName) {
        $(byText(categoryName)).click();

        return this;
    }

    public AddNewPendingPage selectDate(LocalDate date) {

        return calendar.selectDate(date, AddNewPendingPage.class);
    }

    public AddNewPendingPage setDescription(String description) {
        descriptionInput.sendKeys(description);

        return this;
    }

    public MainPage clickSave() {
        saveBtn.click();

        return page(MainPage.class);
    }
}
