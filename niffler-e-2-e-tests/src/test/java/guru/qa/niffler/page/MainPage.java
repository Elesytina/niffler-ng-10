package guru.qa.niffler.page;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selectors.byLinkText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class MainPage {
    private final SelenideElement spendingTable = $("#spendings");
    private final SelenideElement profileIcon = $(byAttribute("data-testid", "PersonIcon"));
    private final SelenideElement createNewSpendingButton = $(byAttribute("href", "http://localhost:3000/spending"));

    public MainPage clickCreateNewSpendingButton() {
        createNewSpendingButton.click();
        return this;
    }

    public ProfilePopupMenuBlock clickProfileIcon() {
        profileIcon.click();
        return new ProfilePopupMenuBlock();
    }

    public void checkThatPageLoaded() {
        spendingTable.should(visible);
    }

    public EditSpendingPage editSpending(String description) {
        spendingTable.$$("tbody tr").find(text(description)).$$("td").get(5).click();
        return new EditSpendingPage();
    }

    public void checkThatTableContains(String description) {
        spendingTable.$$("tbody tr").find(text(description)).should(visible);
    }

    public void checkThatActiveCategoryPresent(){
        $$(By.xpath("//table/tbody/tr")).shouldHave(CollectionCondition.sizeGreaterThanOrEqual(1));
    }

    public static class ProfilePopupMenuBlock {
        private final SelenideElement profileItem = $(byLinkText("Profile"));

        public ProfilePage chooseProfile() {
            profileItem.click();
            return new ProfilePage();
        }
    }
}
