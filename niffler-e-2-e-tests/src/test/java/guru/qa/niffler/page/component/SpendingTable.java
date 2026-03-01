package guru.qa.niffler.page.component;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.enums.DateFilterValues;
import guru.qa.niffler.model.spend.SpendJson;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThanOrEqual;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selenide.$;
import static guru.qa.niffler.model.enums.SpendingColumn.Amount;
import static guru.qa.niffler.model.enums.SpendingColumn.Category;
import static guru.qa.niffler.model.enums.SpendingColumn.Date;
import static guru.qa.niffler.model.enums.SpendingColumn.Description;
import static guru.qa.niffler.utils.DateUtils.getLocalDateFromDate;
import static guru.qa.niffler.utils.DateUtils.getSpendingDateFromString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ParametersAreNonnullByDefault
public class SpendingTable extends BaseComponent<SpendingTable> {

    private final SelenideElement tbody = self.$("tbody");
    private final SelenideElement thead = self.$("thead");
    private final ElementsCollection spendingRows = tbody.$$("tr");
    private final ElementsCollection spendTableColumns = thead.$("tr").$$("th").shouldHave(sizeGreaterThanOrEqual(4));
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

    public void editSpending(String description) {
        ElementsCollection rows = getTableRowByDescription(description).shouldHave(sizeGreaterThanOrEqual(1));

        rows.get(0).$(byAttribute("aria-label", "Edit spending"))
                .click();
    }


    public void checkTableContains(String description) {
        ElementsCollection rows = getTableRowByDescription(description);
        assertFalse(rows.isEmpty(), "There should be at least one row");
    }


    public ElementsCollection getTableRowByDescription(String description) {

        return spendingRows.filterBy(Condition.innerText(description));
    }

    @Nonnull
    public Map<String, Integer> getTableColumnsNames() {
        Map<String, Integer> columnsMap = new HashMap<>();
        AtomicInteger counter = new AtomicInteger();
        spendTableColumns.stream().map(SelenideElement::innerText)
                .forEach(r -> {
                    if (r.isBlank()) {
                        counter.getAndIncrement();
                    } else {
                        columnsMap.put(r, counter.getAndIncrement());
                    }

                });

        return columnsMap;
    }

    @Nonnull
    public String getCellValueByColumnName(int rowIndex, int columnIndex) {
        SelenideElement spendingRow = spendingRows.get(rowIndex);

        return spendingRow.$$("td").get(columnIndex).innerText();
    }

    @Step("verify spending table")
    public void verifySpendingTable(List<SpendJson> spendJsonList) {
        AtomicInteger counter = new AtomicInteger();
        Map<String, Integer> columnsNames = getTableColumnsNames();
        List<Executable> assertions = new ArrayList<>();

        for (SpendJson spendJson : spendJsonList) {
            var actualCategory = getCellValueByColumnName(counter.get(), columnsNames.get(Category.name()));
            var expectedCategory = spendJson.category().name();
            assertions.add(() -> assertEquals(expectedCategory, actualCategory, "For line № %d category".formatted(counter.get())));

            var actualDescription = getCellValueByColumnName(counter.get(), columnsNames.get(Description.name()));
            var expectedDescription = spendJson.description();
            assertions.add(() -> assertEquals(expectedDescription, actualDescription, "For line № %d description".formatted(counter.get())));

            var actualAmount = getCellValueByColumnName(counter.get(), columnsNames.get(Amount.name()));
            DecimalFormat df = new DecimalFormat("0.#");
            var expectedAmount = " %s %s".formatted(df.format(spendJson.amount()), spendJson.currency().getSymbol());
            assertions.add(() -> assertEquals(expectedAmount, actualAmount, "For line № %d amount".formatted(counter.get())));

            LocalDate actualDate = getSpendingDateFromString(getCellValueByColumnName(counter.get(), columnsNames.get(Date.name())));
            LocalDate expectedDate = getLocalDateFromDate(spendJson.spendDate());
            assertions.add(() -> assertEquals(expectedDate, actualDate, "For line № %d date".formatted(counter.get())));

            counter.getAndIncrement();
        }

        Assertions.assertAll(assertions);
    }

}
