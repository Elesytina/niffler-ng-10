package guru.qa.niffler.page.component;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.exception.ScreenshotException;
import guru.qa.niffler.utils.ScreenDiffResult;
import io.qameta.allure.Step;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Selenide.$;

public class StatisticComponent extends BaseComponent<StatisticComponent> {

    private final SelenideElement statisticCanvas = self.$("canvas[role='img']");

    public StatisticComponent() {
        super($("#chart"));
    }

    @Step("verify that statistic image matches the expected image")
    public void verifyStatisticImage(BufferedImage expectedImage) {
        Selenide.sleep(4000);
        try {
            BufferedImage actualImage = ImageIO.read(Objects.requireNonNull(statisticCanvas.screenshot()));
            boolean hasDiff = new ScreenDiffResult(expectedImage, actualImage).getAsBoolean();

            if (hasDiff) {
                throw new ScreenshotException("Image doesn't match expected image");
            }
        } catch (IOException e) {
            throw new RuntimeException("Couldn't read statistics element screenshot", e);
        }
    }
}


