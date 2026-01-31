package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.BasePage;

import static com.codeborne.selenide.Selenide.$;

public class SearchField extends BasePage<SearchField> {

    private final SelenideElement searchBox = $("input");

    public void search(String text) {
        searchBox.sendKeys(text);
        searchBox.submit();
    }
}
