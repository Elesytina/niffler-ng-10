package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.enums.CurrencyValues;
import guru.qa.niffler.page.component.SearchField;
import guru.qa.niffler.page.component.SpendingTable;
import guru.qa.niffler.page.component.StatisticComponent;
import io.qameta.allure.Step;

import java.time.Duration;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThanOrEqual;
import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selectors.byLinkText;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class MainPage extends BasePage<MainPage> {

    private final SelenideElement personIcon = $(byAttribute("data-testid", "PersonIcon"));
    private final SelenideElement profileItem = $(byLinkText("Profile"));
    private final SelenideElement friendsItem = $(byLinkText("Friends"));
    private final SelenideElement allPeopleItem = $(byLinkText("All People"));
    private final SelenideElement currencySearchSelect = $("#currency");
    private final SelenideElement createNewSpendingButton = $(byText("New spending"));
    private final ElementsCollection statisticsItems = $("#legend-container").$$("li");
    private final SearchField searchField = new SearchField();
    private final SpendingTable spendingTable = new SpendingTable();
    private final StatisticComponent statisticComponent = new StatisticComponent();


    @Step("verify spending table is present")
    public void checkThatPageLoaded() {
        spendingTable.checkSpendingVisible();
    }

    @Step("click create new spending")
    public AddNewSpendingPage clickCreateNewSpendingButton() {
        createNewSpendingButton.click();

        return page(AddNewSpendingPage.class);
    }

    @Step("get spendingTable")
    public SpendingTable spendingTable() {
        return spendingTable;
    }


    @Step("search spending by text {text}")
    public MainPage searchSpending(String text) {
        searchField.search(text);

        return this;
    }

    @Step("click edit spending button")
    public EditSpendingPage editSpending(String description) {
        spendingTable.editSpending(description);

        return page(EditSpendingPage.class);
    }

    @Step("delete spending")
    public MainPage deleteSpending(String description) {
        spendingTable.deleteSpending(description);

        return this;
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

    @Step("verify that statistic items contain text {text}")
    public MainPage checkExistingStatisticItems(String... itemTxtList) {
        statisticsItems.shouldHave(sizeGreaterThanOrEqual(1), Duration.ofSeconds(6))
                .shouldHave(texts(itemTxtList));
        return this;
    }

}
