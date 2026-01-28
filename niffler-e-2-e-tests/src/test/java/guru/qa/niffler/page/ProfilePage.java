package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import java.time.Duration;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThanOrEqual;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static java.time.temporal.ChronoUnit.SECONDS;

public class ProfilePage {
    private final SelenideElement showArchived = $(byText("Show archived"));
    private final SelenideElement personIcon = $(byAttribute("data-testid", "PersonIcon"));
    private final SelenideElement nameInput = $("#name");
    private final SelenideElement categoryInput = $("#category");
    private final SelenideElement saveBtn = $(byText("Save changes"));

    @Step("click show archived categories")
    public ProfilePage showArchived() {
        showArchived.click();
        return this;
    }

    @Step("check that profile page is displayed")
    public ProfilePage checkThatPageIsDisplayed() {
        personIcon.shouldBe(visible, Duration.of(10, SECONDS));
        return this;
    }

    @Step("verify that archived categories exist")
    public void checkThatArchivedCategoriesExist() {
        $$(byAttribute("data-testid", "UnarchiveOutlinedIcon"))
                .shouldHave(sizeGreaterThanOrEqual(1));
    }

    @Step("set customer name {name}")
    public ProfilePage editName(String name) {
        nameInput.setValue(name);

        return this;
    }

    @Step("add category with name {categoryName}")
    public ProfilePage addCategory(String categoryName) {
        categoryInput.setValue(categoryName);
        categoryInput.submit();

        return this;
    }

    @Step("click save")
    public ProfilePage save() {
        saveBtn.click();

        return this;
    }

    @Step("verify that profile uploaded")
    public void checkThatProfileUpdated() {
        $(byText("Profile successfully updated"))
                .shouldBe(visible);
    }

    @Step("verify that at least active category presented")
    public void checkThatActiveCategoryPresent(String categoryName) {
        $(withText(categoryName)).shouldBe(visible);
    }
}
