package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.CollectionCondition.sizeGreaterThanOrEqual;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byTagAndText;
import static com.codeborne.selenide.Selectors.byTagName;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class FriendsPage {

    private final SelenideElement friendsTable = $("#friends");
    private final SelenideElement requestTable = $("#requests");
    private final SelenideElement searchInput = $(byTagName("input"));

    public FriendsPage checkFriendsArePresent() {
        friendsTable.$$("tr").shouldHave(sizeGreaterThanOrEqual(1));
        return this;
    }

    public FriendsPage searchFriend(String username) {
        searchInput.sendKeys(username);
        searchInput.submit();

        return page(FriendsPage.class);
    }

    public void checkNameIsPresentInFriendsTable(String name) {
        friendsTable.$$(byTagAndText("p", name)).shouldHave(size(1));
    }

    public void checkNameIsPresentInRequestTable(String name) {
        requestTable.$$(byTagAndText("p", name)).shouldHave(size(1));
    }

    public void checkFriendsTableIsEmpty() {
        $(byText("There are no users yet")).shouldBe(visible);
    }

    public FriendsPage checkRequestsArePresent() {
        requestTable.$$("tr").shouldHave(sizeGreaterThanOrEqual(1));
        return this;
    }

}
