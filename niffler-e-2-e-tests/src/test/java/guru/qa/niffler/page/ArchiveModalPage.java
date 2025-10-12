package guru.qa.niffler.page;

import com.codeborne.selenide.Selectors;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class ArchiveModalPage {

    private final By dialogModal = Selectors.byAttribute("role", "dialog");
    private final By archiveButton = Selectors.byText("Archive");

    public ProfilePage confirmArchive() {
        var button = $(dialogModal).$(archiveButton);
        button.click();
        button.shouldNot(visible);

        $(Selectors.withText("is archived")).shouldBe(visible);
        return page(ProfilePage.class);
    }
}
