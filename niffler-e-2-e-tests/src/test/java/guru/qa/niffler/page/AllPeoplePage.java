package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThanOrEqual;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byTagName;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class AllPeoplePage {
    private final SelenideElement allPeopleTable = $("#all");
    private final ElementsCollection outcomeRequests = allPeopleTable.$$(byText("Waiting..."));
    private final SelenideElement searchInput = $(byTagName("input"));

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
        searchInput.sendKeys(username);
        searchInput.submit();

        return page(AllPeoplePage.class);
    }
}
