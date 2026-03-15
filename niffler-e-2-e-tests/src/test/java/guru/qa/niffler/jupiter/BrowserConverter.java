package guru.qa.niffler.jupiter;

import com.codeborne.selenide.SelenideDriver;
import guru.qa.niffler.model.enums.Browser;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ArgumentConverter;

public class BrowserConverter implements ArgumentConverter {

    @Override
    public Object convert(Object source, ParameterContext context) throws ArgumentConversionException {
        if (context.getParameter().getType().equals(SelenideDriver.class)) {
            if (!(source instanceof Browser browser)) {
                throw new ArgumentConversionException("Wrong argument type");
            }

            return new SelenideDriver(browser.getConfig());

        } else {
            throw new ArgumentConversionException("Unsupported browser type: %s".formatted(source.getClass()));
        }
    }
}
