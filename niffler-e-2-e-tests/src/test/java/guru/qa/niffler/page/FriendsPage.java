package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThanOrEqual;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class FriendsPage {

    private final SelenideElement friendsTable = $("#friends");
    private final SelenideElement requestTable = $("#requests");

    public void checkFriendsArePresent() {
        friendsTable.$$("tr").shouldHave(sizeGreaterThanOrEqual(1));
    }

    public void checkFriendsTableIsEmpty() {
        $(byText("There are no users yet")).shouldBe(visible);
    }

    public void checkRequestsArePresent() {
        requestTable.$$("tr").shouldHave(sizeGreaterThanOrEqual(1));
    }

}
