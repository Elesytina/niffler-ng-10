package guru.qa.niffler.jupiter;

import com.codeborne.selenide.SelenideDriver;
import guru.qa.niffler.model.enums.Browser;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ArgumentConverter;

public class BrowserConverter implements ArgumentConverter {

    @Override
    public SelenideDriver convert(Object source, ParameterContext context) throws ArgumentConversionException {
        if (context.getParameter().getType().equals(SelenideDriver.class)) {
            Browser browser = (Browser) source;

            return new SelenideDriver(browser.getConfig());

        } else {
            throw new IllegalArgumentException("Unsupported browser type: %s".formatted(source.getClass()));
        }
    }
}
