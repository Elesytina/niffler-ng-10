package guru.qa.niffler.page.component;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.enums.DateFilterValues;
import guru.qa.niffler.page.BasePage;
import guru.qa.niffler.page.EditSpendingPage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThanOrEqual;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class SpendingTable extends BasePage<SpendingTable> {

    private final SelenideElement tbody = $("tbody");
    private final ElementsCollection spendingRows = tbody.$$("tr");
    private final SelenideElement deleteBtn = $("#delete");
    private final SelenideElement periodSelect = $("#period");

    public SpendingTable selectPeriod(DateFilterValues dateFilterValues) {
        periodSelect.click();
        $(byAttribute("data-value", dateFilterValues.toString())).click();

        return this;
    }

    public EditSpendingPage editSpending(String description) {
        ElementsCollection rows = getTableRowsByDescription(description).shouldHave(sizeGreaterThanOrEqual(1));

        rows.get(0).$(byAttribute("aria-label", "Edit spending"))
                .click();

        return page(EditSpendingPage.class);
    }

    public SpendingTable deleteSpending(String description) {
        ElementsCollection rows = getTableRowsByDescription(description).shouldHave(sizeGreaterThanOrEqual(1));
        rows.get(0).$(byAttribute("type", "checkbox")).click();
        deleteBtn.click();

        return this;
    }

    public SpendingTable checkTableContains(String description) {
        ElementsCollection rows = getTableRowsByDescription(description);
        Assertions.assertFalse(rows.isEmpty(), "There should be at least one row");

        return this;
    }

    public SpendingTable checkTableSize(int expSize) {
        int actualSize = spendingRows.size();
        Assertions.assertEquals(actualSize, expSize, "Expected %d rows count but found %d".formatted(expSize, actualSize));

        return this;
    }

    private ElementsCollection getTableRowsByDescription(String description) {

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
