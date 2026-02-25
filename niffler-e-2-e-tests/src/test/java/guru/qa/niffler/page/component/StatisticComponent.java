package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.exception.ScreenshotException;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.utils.ScreenDiffResult;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.Duration;
import java.util.Objects;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThanOrEqual;
import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Selenide.$;

public class StatisticComponent extends BaseComponent<StatisticComponent> {

    private final SelenideElement statisticCanvas = self.$("canvas[role='img']");

    public StatisticComponent() {
        super($("#chart"));
    }

    @Step("verify that statistic image matches the expected image")
    public void verifyStatisticImage(BufferedImage expectedImage) {
        BufferedImage actualImage = getChartScreenshot();
        boolean hasDiff = new ScreenDiffResult(expectedImage, actualImage).getAsBoolean();

        if (hasDiff) {
            throw new ScreenshotException("Image doesn't match expected image");
        }
    }

    @Nonnull
    @Step("get statistics img screenshot")
    public BufferedImage getChartScreenshot() {
        try {
            Selenide.sleep(4000);
            return ImageIO.read(Objects.requireNonNull(statisticCanvas.screenshot()));
        } catch (IOException e) {
            throw new RuntimeException("Couldn't read statistics element screenshot", e);
        }
    }

}


