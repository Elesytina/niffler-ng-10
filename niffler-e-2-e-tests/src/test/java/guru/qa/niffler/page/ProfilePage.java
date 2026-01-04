package guru.qa.niffler.page;

import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThanOrEqual;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static java.time.temporal.ChronoUnit.SECONDS;

public class ProfilePage {
    private final SelenideElement showArchived = $(byText("Show archived"));
    private final SelenideElement personIcon = $(byAttribute("data-testid", "PersonIcon"));
    private final SelenideElement nameInput = $("#name");
    private final SelenideElement categoryInput = $("#category");
    private final SelenideElement saveBtn = $(Selectors.byText("Save changes"));

    public ProfilePage showArchived() {
        showArchived.click();
        return this;
    }

    public ProfilePage checkThatPageIsDisplayed() {
        personIcon.shouldBe(visible, Duration.of(10, SECONDS));
        return this;
    }

    public void checkThatArchivedCategoriesExist() {
        $$(byAttribute("data-testid", "UnarchiveOutlinedIcon"))
                .shouldHave(sizeGreaterThanOrEqual(1));
    }

    public ProfilePage editName(String name) {
        nameInput.setValue(name);

        return this;
    }

    public ProfilePage addCategory(String categoryName) {
        categoryInput.setValue(categoryName);
        categoryInput.submit();

        return this;
    }

    public ProfilePage save() {
        saveBtn.click();

        return this;
    }

    public void checkThatProfileUpdated() {
        $(byText("Profile successfully updated"))
                .shouldBe(visible);
    }
}
