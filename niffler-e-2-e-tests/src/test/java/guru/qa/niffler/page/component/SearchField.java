package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.BasePage;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class SearchField extends BasePage<SearchField> {

    private final SelenideElement searchBox = $("input");

    public <T> void search(String text, Class<T> nextPage) {
        searchBox.sendKeys(text);
        searchBox.submit();

        page(nextPage);
    }
}
