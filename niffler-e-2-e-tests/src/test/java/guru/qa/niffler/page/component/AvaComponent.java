package guru.qa.niffler.page.component;

import guru.qa.niffler.exception.ScreenshotException;
import guru.qa.niffler.utils.ScreenDiffResult;
import io.qameta.allure.Step;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Selenide.$;

public class AvaComponent extends BaseComponent<AvaComponent> {


    public AvaComponent() {
        super($("img[class]"));
    }

    @Step("verify avatar img")
    public void verifyAvatarImg(BufferedImage expectedImg) {
        try {
            BufferedImage actualImage = ImageIO.read(Objects.requireNonNull(self.screenshot()));
            boolean hasDiff = new ScreenDiffResult(expectedImg, actualImage).getAsBoolean();
            if (hasDiff) {
                throw new ScreenshotException("Image doesn't match expected image");
            }
        } catch (IOException e) {
            throw new RuntimeException("Couldn't read avatar element screenshot", e);
        }
    }
}

