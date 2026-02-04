package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.utils.ScreenDiffResult;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.time.Duration;
import java.util.Objects;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThanOrEqual;
import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Selenide.$;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class StatisticComponent extends BaseComponent<StatisticComponent> {

    private final SelenideElement statisticCanvas = $("canvas[role='img']");

    private final ElementsCollection statisticsItems = self.$("#legend-container").$$("li");

    public StatisticComponent() {
        super($("#stat"));
    }

    @Step("verify that statistic items contain text {text}")
    public @Nonnull StatisticComponent checkStatisticItems(String... texts) {
        statisticsItems.shouldHave(sizeGreaterThanOrEqual(1), Duration.ofSeconds(6))
                .shouldHave(texts(texts));
        return this;
    }

    @Step("verify that statistic image matches the expected image")
    public void verifyStatisticImage(BufferedImage expectedImage) {
        try {
            //  BufferedImage actualImage = awaitForEvent(statisticCanvas::screenshotAsImage);
            Selenide.sleep(5000);
            BufferedImage actualImage = ImageIO.read(Objects.requireNonNull(statisticCanvas.screenshot()));

            assertFalse(
                    new ScreenDiffResult(
                            expectedImage,
                            actualImage
                    )
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

