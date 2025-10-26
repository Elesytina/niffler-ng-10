package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThanOrEqual;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selectors.byLinkText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class MainPage {
    private final SelenideElement spendingTable = $("#spendings");
    private final SelenideElement personIcon = $(byAttribute("data-testid", "PersonIcon"));
    private final SelenideElement profileItem = $(byLinkText("Profile"));
    private final SelenideElement friendsItem = $(byLinkText("Friends"));
    private final SelenideElement allPeopleItem = $(byLinkText("All People"));
    private final SelenideElement createNewSpendingButton = $(byAttribute("href", "http://localhost:3000/spending"));

    public void checkThatPageLoaded() {
        spendingTable.should(visible);
    }

    public MainPage clickCreateNewSpendingButton() {
        createNewSpendingButton.click();
        return this;
    }

    public EditSpendingPage editSpending(String description) {
        spendingTable.$$("tbody tr").find(text(description)).$$("td").get(5).click();
        return new EditSpendingPage();
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
