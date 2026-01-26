package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class SearchField {

    private final SelenideElement searchBox = $("input");

    public void search(String text) {
        searchBox.sendKeys(text);
        searchBox.submit();
    }
}
