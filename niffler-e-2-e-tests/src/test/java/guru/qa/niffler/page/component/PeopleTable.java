package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.FriendsPage;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.CollectionCondition.sizeGreaterThanOrEqual;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byTagAndText;
import static com.codeborne.selenide.Selectors.byTagName;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class PeopleTable extends BaseComponent<PeopleTable> {

    private final SelenideElement allPeopleTable = self.$("#all");
    private final SelenideElement friendsTable = self.$("#friends");
    private final SelenideElement requestTable = self.$("#requests");
    private final SelenideElement unfriendBtn = self.$(byText("Unfriend"));
    private final SelenideElement acceptBtn = self.$(byText("Accept"));
    private final SelenideElement declineBtn = self.$(byText("Decline"));
    private final Modal submitModal = new Modal();

    public PeopleTable() {
        super($(byTagName("table")));
    }

    public void checkNameIsPresentInOutcomeRequests(String name) {
        allPeopleTable.$$(byTagName("tr"))
                .filterBy(text(name))
                .filterBy(text("Waiting..."))
                .shouldHave(sizeGreaterThanOrEqual(1));
    }

    public void checkNameIsPresentInFriendsTable(String name) {
        friendsTable.$$(byTagAndText("p", name)).shouldHave(size(1));
    }

    public FriendsPage checkRequestsArePresent() {
        requestTable.$$("tr").shouldHave(sizeGreaterThanOrEqual(1));

        return page(FriendsPage.class);
    }

    public FriendsPage checkFriendsArePresent() {
        friendsTable.$$("tr").shouldHave(sizeGreaterThanOrEqual(1));

        return page(FriendsPage.class);
    }

    public void checkNameIsPresentInRequestTable(String name) {
        requestTable.$$(byTagAndText("p", name)).shouldHave(size(1));
    }

    public void checkNameIsAbsentInRequestTable(String name) {
        requestTable.$$(byTagAndText("p", name)).shouldHave(size(0));
    }

    public PeopleTable acceptRequest() {
        acceptBtn.click();

        return this;
    }

    public PeopleTable declineRequest() {
        declineBtn.click();
        submitModal.decline();

        return this;
    }
}
