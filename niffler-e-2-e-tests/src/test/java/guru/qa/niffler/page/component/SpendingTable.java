package guru.qa.niffler.page.component;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.enums.DateFilterValues;
import guru.qa.niffler.page.EditSpendingPage;
import io.qameta.allure.Step;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThanOrEqual;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class SpendingTable extends BaseComponent<SpendingTable> {

    private final SelenideElement tbody = self.$("tbody");
    private final ElementsCollection spendingRows = tbody.$$("tr");
    private final SelenideElement deleteBtn = self.$("#delete");
    private final SelenideElement periodSelect = self.$("#period");
    private final SubmitModal submitModal = new SubmitModal();

    public SpendingTable() {
        super($("#spendings"));
    }

    public SpendingTable selectPeriod(DateFilterValues dateFilterValues) {
        periodSelect.click();
        $(byAttribute("data-value", dateFilterValues.toString())).click();

        return this;
    }

    public void checkSpendingVisible() {
        self.should(visible);
    }

    @Step("delete spending")
    public void deleteSpending(String description) {
        ElementsCollection rows = getTableRowByDescription(description).shouldHave(sizeGreaterThanOrEqual(1));
        rows.get(0).$(byAttribute("type", "checkbox")).click();
        deleteBtn.click();
        submitModal.submit("Delete");
    }

    public EditSpendingPage editSpending(String description) {
        ElementsCollection rows = getTableRowByDescription(description).shouldHave(sizeGreaterThanOrEqual(1));

        rows.get(0).$(byAttribute("aria-label", "Edit spending"))
                .click();

        return page(EditSpendingPage.class);
    }


    public void checkTableContains(String description) {
        ElementsCollection rows = getTableRowByDescription(description);
        Assertions.assertFalse(rows.isEmpty(), "There should be at least one row");
    }


    public ElementsCollection getTableRowByDescription(String description) {

        return spendingRows.filterBy(Condition.innerText(description));
    }

    @Getter
    @RequiredArgsConstructor
    private enum Column {
        CATEGORY("Category"),
        AMOUNT("Amount"),
        DESCRIPTION("Description"),
        DATE("Date");

        private final String value;
    }
}
