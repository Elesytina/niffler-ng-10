package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.BasePage;
import guru.qa.niffler.page.FriendsPage;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.CollectionCondition.sizeGreaterThanOrEqual;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selectors.byTagAndText;
import static com.codeborne.selenide.Selectors.byTagName;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class PeopleTable extends BasePage<PeopleTable> {

    private final SelenideElement allPeopleTable = $("#all");
    private final ElementsCollection outcomeRequests = allPeopleTable.$$(byText("Waiting..."));
    private final SelenideElement friendsTable = $("#friends");
    private final SelenideElement requestTable = $("#requests");
    private final SelenideElement acceptBtn = $(byText("Accept"));
    private final SelenideElement declineBtn = $(byText("Decline"));
    private final SelenideElement unfriendBtn = $(byText("Unfriend"));
    private final SelenideElement submitModal = $(byAttribute("role", "dialog"));

    public <T> T checkThatOutcomeRequestsArePresent(Class<T> nextPage) {
        outcomeRequests.shouldHave(sizeGreaterThanOrEqual(1));

        return page(nextPage);
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
        unfriendBtn.shouldBe(visible);

        return this;
    }

    public PeopleTable declineRequest() {
        declineBtn.click();
        submitModal.shouldBe(visible)
                .find(byText("Decline"))
                .click();

        return this;
    }
}
