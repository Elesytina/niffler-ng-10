package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selenide.$;

public class Calendar extends BaseComponent<Calendar> {

    public Calendar() {
        super($(byName("date")));
    }

    public void selectDate(LocalDate date) {
        self.click();
        self.sendKeys(Keys.CONTROL + "a");
        self.sendKeys(Keys.DELETE);
        self.sendKeys(date.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
    }
}
