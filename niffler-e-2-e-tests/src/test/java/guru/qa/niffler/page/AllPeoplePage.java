package guru.qa.niffler.page;

import guru.qa.niffler.page.component.PeopleTable;
import guru.qa.niffler.page.component.SearchField;
import io.qameta.allure.Step;

public class AllPeoplePage extends BasePage<AllPeoplePage> {

    private final SearchField searchField = new SearchField();
    private final PeopleTable peopleTable = new PeopleTable();

    @Step("verify that name {name} is presented in outcome requests")
    public void checkNameIsPresentedInOutcomeRequests(String name) {
        peopleTable.checkNameIsPresentInOutcomeRequests(name);
    }

    @Step("search requests by username {username}")
    public AllPeoplePage searchRequest(String username) {
        searchField.search(username);

        return this;
    }
}
