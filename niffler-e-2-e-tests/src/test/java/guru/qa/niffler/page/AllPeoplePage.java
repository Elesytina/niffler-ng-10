package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.SearchField;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThanOrEqual;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byTagName;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class AllPeoplePage {
    private final SelenideElement allPeopleTable = $("#all");
    private final ElementsCollection outcomeRequests = allPeopleTable.$$(byText("Waiting..."));
    private final SearchField searchField = new SearchField();

    public AllPeoplePage checkThatOutcomeRequestsArePresent() {
        outcomeRequests.shouldHave(sizeGreaterThanOrEqual(1));
        return this;
    }

    public void checkNameIsPresentInOutcomeRequests(String name) {
        allPeopleTable.$$(byTagName("tr"))
                .filterBy(text(name))
                .filterBy(text("Waiting..."))
                .shouldHave(sizeGreaterThanOrEqual(1));
    }

    public AllPeoplePage searchRequest(String username) {
        searchField.search(username, AllPeoplePage.class);

        return page(AllPeoplePage.class);
    }
}
