package guru.qa.niffler.condition;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementCondition;
import com.codeborne.selenide.WebElementsCondition;
import guru.qa.niffler.model.Bubble;
import org.apache.commons.lang.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static guru.qa.niffler.condition.Color.getColor;

@ParametersAreNonnullByDefault
public class StatCondition {

    public static WebElementCondition color(Color expectedColor) {
        return new WebElementCondition("color") {

            @NotNull
            @Override
            public CheckResult check(Driver driver, WebElement element) {
                final String rgba = element.getCssValue("background-color");

                return new CheckResult(expectedColor.getRgba().equals(rgba), rgba);
            }
        };

    }

    public static WebElementsCondition colors(Color... expectedColors) {
        return new WebElementsCondition() {

            @NotNull
            @Override
            public CheckResult check(Driver driver, List<WebElement> elements) {
                if (ArrayUtils.isEmpty(expectedColors)) {
                    throw new IllegalArgumentException("Expected at least one color");
                }

                if (elements.size() != expectedColors.length) {
                    return new CheckResult(false, "Expected %d elements, but found %d".formatted(expectedColors.length, elements.size()));
                }

                List<String> expectedRgbaList = Arrays.stream(expectedColors).map(Color::getRgba).toList();
                List<Boolean> resultCollector = new ArrayList<>();
                List<String> rgbaList = new ArrayList<>();
                for (int i = 0; i < expectedRgbaList.size(); i++) {
                    String rgba = elements.get(i).getCssValue("background-color");
                    rgbaList.add(rgba);
                    resultCollector.add(rgba.equals(expectedRgbaList.get(i)));
                }

                return new CheckResult(resultCollector.stream().allMatch(r -> r), rgbaList);
            }

            @NotNull
            @Override
            public String toString() {
                return "Colors: %s".formatted(Arrays.stream(expectedColors).map(Color::getRgba).toList());
            }
        };

    }

    public static WebElementsCondition bubbles(Bubble... expectedBubbles) {
        return new WebElementsCondition() {

            @NotNull
            @Override
            public CheckResult check(Driver driver, List<WebElement> elements) {
                if (ArrayUtils.isEmpty(expectedBubbles)) {
                    throw new IllegalArgumentException("Expected at least one bubble");
                }

                if (elements.size() != expectedBubbles.length) {
                    return new CheckResult(false, "Expected %d elements, but found %d".formatted(expectedBubbles.length, elements.size()));
                }

                List<Boolean> resultCollector = new ArrayList<>();
                List<Bubble> actualBubbles = new ArrayList<>();
                for (int i = 0; i < expectedBubbles.length; i++) {
                    String rgba = elements.get(i).getCssValue("background-color");
                    Bubble actual = new Bubble(getColor(rgba), elements.get(i).getText());
                    actualBubbles.add(actual);
                    resultCollector.add(actual.equals(expectedBubbles[i]));
                }

                return new CheckResult(resultCollector.stream().allMatch(r -> r), actualBubbles);
            }

            @NotNull
            @Override
            public String toString() {
                return "Bubbles: %s".formatted(Arrays.stream(expectedBubbles).toList());
            }
        };

    }

    public static WebElementsCondition bubblesInAnyOrder(Bubble... expectedBubbles) {
        return new WebElementsCondition() {

            @NotNull
            @Override
            public CheckResult check(Driver driver, List<WebElement> elements) {
                if (ArrayUtils.isEmpty(expectedBubbles)) {
                    throw new IllegalArgumentException("Expected at least one bubble");
                }

                if (elements.size() != expectedBubbles.length) {
                    return new CheckResult(false, "Expected %d elements, but found %d".formatted(expectedBubbles.length, elements.size()));
                }

                List<Bubble> actualBubbles = elements.stream()
                        .map(el -> new Bubble(
                                getColor(el.getCssValue("background-color")),
                                el.getText()
                        ))
                        .toList();

                boolean result = actualBubbles.containsAll(List.of(expectedBubbles));

                return new CheckResult(result, actualBubbles);
            }

            @NotNull
            @Override
            public String toString() {
                return "Bubbles: %s".formatted(List.of(expectedBubbles));
            }
        };

    }

    public static WebElementsCondition bubblesContains(Bubble... expectedBubbles) {
        return new WebElementsCondition() {

            @NotNull
            @Override
            public CheckResult check(Driver driver, List<WebElement> elements) {
                if (ArrayUtils.isEmpty(expectedBubbles)) {
                    throw new IllegalArgumentException("Expected at least one bubble");
                }

                if (elements.size() < expectedBubbles.length) {
                    return new CheckResult(false, "Expected more than or equal %d elements, but found %d".formatted(expectedBubbles.length, elements.size()));
                }

                List<Bubble> actualBubbles = elements.stream()
                        .map(el -> new Bubble(
                                getColor(el.getCssValue("background-color")),
                                el.getText()
                        ))
                        .toList();

                boolean result = actualBubbles.containsAll(List.of(expectedBubbles));

                return new CheckResult(result, actualBubbles);
            }

            @NotNull
            @Override
            public String toString() {
                return "Bubbles: %s".formatted(List.of(expectedBubbles));
            }
        };

    }
}
