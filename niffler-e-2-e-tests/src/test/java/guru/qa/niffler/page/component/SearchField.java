package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class SearchField extends BaseComponent<SearchField> {

    private final SelenideElement searchBox = self.$("input");

    public SearchField() {
        super($("form"));
    }

    public void search(String text) {
        searchBox.sendKeys(text);
        searchBox.submit();
    }
}
