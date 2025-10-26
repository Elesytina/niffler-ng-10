package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.hibernate.AssertionFailure;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThanOrEqual;
import static com.codeborne.selenide.Selectors.byTagName;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class AllPeoplePage {
    private final SelenideElement allPeopleTable = $("#all");
    private final ElementsCollection outcomeRequests = allPeopleTable.$$(byText("Waiting..."));

    public AllPeoplePage checkThatOutcomeRequestsArePresent() {
        outcomeRequests.shouldHave(sizeGreaterThanOrEqual(1));
        return this;
    }

    public void checkNameIsPresentInOutcomeRequests(String name) {
        var lineList = allPeopleTable.$$(byTagName("tr"));
        for (SelenideElement element : lineList) {
            if (element.getText().contains(name) && element.getText().contains("Waiting...")) {
                return;
            }
        }
        throw new AssertionFailure("No such outcome request: " + name);
    }
}
