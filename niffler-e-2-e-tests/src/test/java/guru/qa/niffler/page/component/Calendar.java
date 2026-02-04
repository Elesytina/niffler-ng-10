package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.BasePage;
import org.openqa.selenium.Keys;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selenide.$;

public class Calendar extends BasePage<Calendar> {

    private final SelenideElement calendarInput = $(byName("date"));

    public void selectDate(LocalDate date) {
        calendarInput.click();
        calendarInput.sendKeys(Keys.CONTROL + "a");
        calendarInput.sendKeys(Keys.DELETE);
        calendarInput.sendKeys(date.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
    }
}
