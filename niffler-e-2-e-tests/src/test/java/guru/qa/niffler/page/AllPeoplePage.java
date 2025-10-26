package guru.qa.niffler.page;

import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThanOrEqual;
import static com.codeborne.selenide.Selenide.$;

public class AllPeoplePage {
    private final SelenideElement allPeopleTable = $("#all");

    public AllPeoplePage checkThatOutcomeRequestsArePresent() {
        allPeopleTable.$$(Selectors.byText("Waiting...")).shouldHave(sizeGreaterThanOrEqual(1));
        return this;
    }
}
