package guru.qa.niffler.page;

import guru.qa.niffler.page.component.PeopleTable;
import guru.qa.niffler.page.component.SearchField;

import static com.codeborne.selenide.Selenide.page;

public class AllPeoplePage {
    private final SearchField searchField = new SearchField();
    private final PeopleTable peopleTable = new PeopleTable();

    public AllPeoplePage checkThatOutcomeRequestsArePresent() {

        return peopleTable.checkThatOutcomeRequestsArePresent(AllPeoplePage.class);
    }

    public void checkNameIsPresentInOutcomeRequests(String name) {
        peopleTable.checkNameIsPresentInOutcomeRequests(name);
    }

    public AllPeoplePage searchRequest(String username) {
        searchField.search(username, AllPeoplePage.class);

        return page(AllPeoplePage.class);
    }
}
