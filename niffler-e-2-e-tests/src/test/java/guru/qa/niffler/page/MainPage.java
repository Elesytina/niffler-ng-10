package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selectors.byLinkText;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {
    private final SelenideElement spendingTable = $("#spendings");
    private final SelenideElement personIcon = $(byAttribute("data-testid", "PersonIcon"));

    public MainPage checkThatPageLoaded() {
        spendingTable.should(visible);
        return this;
    }

    public EditSpendingPage editSpending(String description) {
        spendingTable.$$("tbody tr").find(text(description)).$$("td").get(5).click();
        return new EditSpendingPage();
    }

    public MainPage checkThatTableContains(String description) {
        spendingTable.$$("tbody tr").find(text(description)).should(visible);
        return this;
    }

    public ProfilePopupMenuBlock openProfilePopupMenu() {
        personIcon.click();
        return new ProfilePopupMenuBlock();
    }

    public class ProfilePopupMenuBlock {
        private final SelenideElement profileItem = $(byLinkText("Profile"));
        private final SelenideElement friendsItem = $(byLinkText("Friends"));
        private final SelenideElement allPeopleItem = $(byLinkText("All People"));

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
    }
}
