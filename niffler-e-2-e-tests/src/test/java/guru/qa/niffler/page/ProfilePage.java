package guru.qa.niffler.page;

import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$$;

public class ProfilePage {
    private final By uploadNewPictureButton = Selectors.byText("Upload new picture");
    private final By registerPassKeyButton = Selectors.byText("Register Passkey");
    private final By saveChangesButton = Selectors.byText("Save changes");
    private final By nameInput = Selectors.byId("name");
    private final By userNameDisabledInput = Selectors.byId("username");
    private final By categoryInput = Selectors.byId("category");
    private final By showArchived = Selectors.byText("Show archived");
    private final By editCategoryButton = Selectors.byAttribute("aria-label", "Edit category");
    private final By archiveCategoryButton = Selectors.byAttribute("aria-label", "Archive category");

    public ProfilePage clickOnArchiveCategory() {
        return this;
    }

    public ProfilePage archiveCategory(String categoryName) {
        var selector =Selectors.byText(categoryName);
        var categoryList = $$(selector);
        for(SelenideElement element : categoryList) {
            element.$(archiveCategoryButton).click();
        }
        return this;
    }

}
