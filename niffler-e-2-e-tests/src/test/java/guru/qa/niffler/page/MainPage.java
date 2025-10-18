package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {

    private final SelenideElement spendingTable = $("#spendings");
    private final SelenideElement statistics = $("#chart");

    public void checkThatPageLoaded() {
        spendingTable.shouldBe(visible);
        statistics.shouldBe(visible);
    }

    public EditSpendingPage editSpending(String description) {
        spendingTable.$$("tbody tr").find(text(description)).$$("td").get(5).click();
        return new EditSpendingPage();
    }

    public void checkThatTableContains(String description) {
        spendingTable.$$("tbody tr").find(text(description)).should(visible);
    }
}
