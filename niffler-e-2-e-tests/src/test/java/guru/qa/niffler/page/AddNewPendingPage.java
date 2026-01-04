package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.enums.CurrencyValues;
import guru.qa.niffler.page.component.Calendar;
import io.qameta.allure.Step;

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

    @Step("set spending amount")
    public AddNewPendingPage setAmount(int amount) {
        amountInput.sendKeys(String.valueOf(amount));

        return this;
    }

    @Step("set spending currency")
    public AddNewPendingPage selectCurrency(CurrencyValues currency) {
        currencySelect.click();
        $(byAttribute("data-value", currency.toString()))
                .click();

        return this;
    }

    @Step("select spending category")
    public AddNewPendingPage selectCategory(String categoryName) {
        $(byText(categoryName)).click();

        return this;
    }

    @Step("set spending date")
    public AddNewPendingPage selectDate(LocalDate date) {

        return calendar.selectDate(date, AddNewPendingPage.class);
    }

    @Step("set spending description")
    public AddNewPendingPage setDescription(String description) {
        descriptionInput.sendKeys(description);

        return this;
    }

    @Step("click save")
    public MainPage clickSave() {
        saveBtn.click();

        return page(MainPage.class);
    }
}
