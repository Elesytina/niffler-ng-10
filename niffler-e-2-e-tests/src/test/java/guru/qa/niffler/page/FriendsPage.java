package guru.qa.niffler.page;

import guru.qa.niffler.page.component.PeopleTable;
import guru.qa.niffler.page.component.SearchField;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class FriendsPage {

    private final SearchField searchField = new SearchField();
    private final PeopleTable peopleTable = new PeopleTable();

    public FriendsPage checkFriendsArePresent() {

        return peopleTable.checkFriendsArePresent();
    }

    public FriendsPage searchFriend(String username) {
        searchField.search(username, FriendsPage.class);

        return page(FriendsPage.class);
    }

    public void checkNameIsPresentInFriendsTable(String name) {
        peopleTable.checkNameIsPresentInFriendsTable(name);
    }

    public PeopleTable checkNameIsPresentInRequestTable(String name) {
        peopleTable.checkNameIsPresentInRequestTable(name);

        return page(PeopleTable.class);
    }

    public void checkFriendsTableIsEmpty() {
        $(byText("There are no users yet")).shouldBe(visible);
    }

    public FriendsPage checkRequestsArePresent() {

        return peopleTable.checkRequestsArePresent();
    }

}
