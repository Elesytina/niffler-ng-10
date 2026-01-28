package guru.qa.niffler.page;

import guru.qa.niffler.page.component.PeopleTable;
import guru.qa.niffler.page.component.SearchField;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class FriendsPage {

    private final SearchField searchField = new SearchField();
    private final PeopleTable peopleTable = new PeopleTable();

    @Step("verify that friends are presented")
    public FriendsPage checkFriendsArePresented() {

        return peopleTable.checkFriendsArePresent();
    }

    @Step("search friend by username {username}")
    public FriendsPage searchFriend(String username) {
        searchField.search(username);

        return this;
    }

    @Step("verify that name {name} is presented in friends table")
    public void checkNameIsPresentedInFriendsTable(String name) {
        peopleTable.checkNameIsPresentInFriendsTable(name);
    }

    @Step("verify that name {name} is presented in requests table")
    public PeopleTable checkNameIsPresentedInRequestTable(String name) {
        peopleTable.checkNameIsPresentInRequestTable(name);

        return page(PeopleTable.class);
    }

    @Step("verify that friends table is empty")
    public void checkFriendsTableIsEmpty() {
        $(byText("There are no users yet")).shouldBe(visible);
    }

    @Step("verify requests are presented")
    public FriendsPage checkRequestsArePresented() {

        return peopleTable.checkRequestsArePresent();
    }

}
