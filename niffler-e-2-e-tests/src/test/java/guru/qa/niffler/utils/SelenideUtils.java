package guru.qa.niffler.utils;

import com.codeborne.selenide.SelenideConfig;

public class SelenideUtils {

    public static SelenideConfig CHROME_CONFIG = new SelenideConfig()
            .browser("chrome")
            .pageLoadStrategy("eager")
            .timeout(5000);

    public static SelenideConfig FIREFOX_CONFIG = new SelenideConfig()
            .browser("firefox")
            .pageLoadStrategy("eager")
            .timeout(5000);
}
