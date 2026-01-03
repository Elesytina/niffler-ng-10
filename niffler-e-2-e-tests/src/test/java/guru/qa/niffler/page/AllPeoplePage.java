package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.PeopleTable;
import guru.qa.niffler.page.component.SearchField;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
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
