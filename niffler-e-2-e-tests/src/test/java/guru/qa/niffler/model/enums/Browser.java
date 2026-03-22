package guru.qa.niffler.model.enums;

import com.codeborne.selenide.SelenideConfig;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static guru.qa.niffler.utils.SelenideUtils.CHROME_CONFIG;
import static guru.qa.niffler.utils.SelenideUtils.FIREFOX_CONFIG;

@Getter
@RequiredArgsConstructor
public enum Browser {

    CHROME(CHROME_CONFIG),
    FIREFOX(FIREFOX_CONFIG);

    private final SelenideConfig config;
}
