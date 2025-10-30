package guru.qa.niffler.page;

import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.CollectionCondition.sizeGreaterThanOrEqual;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class FriendsPage {

    private final SelenideElement friendsTable = $("#friends");
    private final SelenideElement requestTable = $("#requests");

    public FriendsPage checkFriendsArePresent() {
        friendsTable.$$("tr").shouldHave(sizeGreaterThanOrEqual(1));
        return this;
    }

    public void checkNameIsPresentInFriendsTable(String name) {
        friendsTable.$$(Selectors.byTagAndText("p", name)).shouldHave(size(1));
    }

    public void checkNameIsPresentInRequestTable(String name) {
        requestTable.$$(Selectors.byTagAndText("p", name)).shouldHave(size(1));
    }

    public void checkFriendsTableIsEmpty() {
        $(byText("There are no users yet")).shouldBe(visible);
    }

    public FriendsPage checkRequestsArePresent() {
        requestTable.$$("tr").shouldHave(sizeGreaterThanOrEqual(1));
        return this;
    }

}
