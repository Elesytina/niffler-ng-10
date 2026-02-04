package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.enums.CurrencyValues;
import guru.qa.niffler.page.component.SearchField;
import guru.qa.niffler.page.component.StatisticComponent;
import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThanOrEqual;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selectors.byLinkText;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.page;

public class MainPage extends BasePage<MainPage> {

    private final SelenideElement spendingTable = $("#spendings");
    private final SelenideElement personIcon = $(byAttribute("data-testid", "PersonIcon"));
    private final SelenideElement profileItem = $(byLinkText("Profile"));
    private final SelenideElement friendsItem = $(byLinkText("Friends"));
    private final SelenideElement allPeopleItem = $(byLinkText("All People"));
    private final SelenideElement currencySearchSelect = $("#currency");
    private final SelenideElement createNewSpendingButton = $(byText("New spending"));
    private final SearchField searchField = new SearchField();
    private final StatisticComponent statisticComponent = new StatisticComponent();

    @Step("verify spending table is present")
    public void checkThatPageLoaded() {
        spendingTable.should(visible);
    }

    @Step("click create new spending")
    public AddNewSpendingPage clickCreateNewSpendingButton() {
        createNewSpendingButton.click();

        return page(AddNewSpendingPage.class);
    }

    @Step("search spending by text {text}")
    public MainPage searchSpending(String text) {
        searchField.search(text);

        return this;
    }

    @Step("click edit spending button")
    public EditSpendingPage editSpending() {
        $$(byAttribute("aria-label", "Edit spending"))
                .shouldHave(sizeGreaterThanOrEqual(1))
                .get(0)
                .click();

        return page(EditSpendingPage.class);
    }

    @Step("verify that spending table contains description {description}")
    public void checkThatTableContains(String description) {
        spendingTable.$$("tbody tr").find(text(description)).should(visible);
    }

    @Step("open profile popup menu")
    public MainPage openProfilePopupMenu() {
        personIcon.click();
        return this;
    }

    @Step("choose profile item")
    public ProfilePage chooseProfile() {
        profileItem.click();
        return new ProfilePage();
    }

    @Step("choose friends item")
    public FriendsPage chooseFriends() {
        friendsItem.click();
        return new FriendsPage();
    }

    @Step("choose all people item")
    public AllPeoplePage chooseAllPeople() {
        allPeopleItem.click();
        return new AllPeoplePage();
    }

    @Step("get statistic component")
    public StatisticComponent statisticComponent() {
        return statisticComponent;
    }

    @Step("select currency")
    public MainPage selectCurrency(CurrencyValues currency) {
        currencySearchSelect.click();
        $(byAttribute("data-value", currency.name())).click();

        return this;
    }
}
