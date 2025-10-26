package guru.qa.niffler.page;

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
}
