package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.SearchField;
import org.openqa.selenium.By;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThanOrEqual;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selectors.byLinkText;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.page;

public class MainPage {

    private final SelenideElement spendingTable = $("#spendings");
    private final SelenideElement personIcon = $(byAttribute("data-testid", "PersonIcon"));
    private final SelenideElement profileItem = $(byLinkText("Profile"));
    private final SelenideElement friendsItem = $(byLinkText("Friends"));
    private final SelenideElement allPeopleItem = $(byLinkText("All People"));
    private final SelenideElement createNewSpendingButton = $(byText("New spending"));
    private final SearchField searchField = new SearchField();

    public void checkThatPageLoaded() {
        spendingTable.should(visible);
    }

    public AddNewPendingPage clickCreateNewSpendingButton() {
        createNewSpendingButton.click();

        return page(AddNewPendingPage.class);
    }

    public MainPage searchSpending(String text) {
        searchField.search(text, MainPage.class);

        return this;
    }

    public EditSpendingPage editSpending() {
        $$(byAttribute("aria-label", "Edit spending"))
                .shouldHave(sizeGreaterThanOrEqual(1))
                .get(0)
                .click();

        return page(EditSpendingPage.class);
    }

    public void checkThatTableContains(String description) {
        spendingTable.$$("tbody tr").find(text(description)).should(visible);
    }

    public MainPage openProfilePopupMenu() {
        personIcon.click();
        return this;
    }

    public ProfilePage chooseProfile() {
        profileItem.click();
        return new ProfilePage();
    }

    public FriendsPage chooseFriends() {
        friendsItem.click();
        return new FriendsPage();
    }

    public AllPeoplePage chooseAllPeople() {
        allPeopleItem.click();
        return new AllPeoplePage();
    }

    public void checkThatActiveCategoryPresent() {
        $$(By.xpath("//table/tbody/tr")).shouldHave(sizeGreaterThanOrEqual(1));
    }
}
