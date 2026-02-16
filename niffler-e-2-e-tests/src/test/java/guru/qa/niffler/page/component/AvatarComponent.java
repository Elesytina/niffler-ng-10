package guru.qa.niffler.page.component;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.utils.ScreenDiffResult;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Selenide.$;

public class AvatarComponent extends BaseComponent<AvatarComponent> {

    public AvatarComponent() {
        super($(".MuiAvatar-img"));
    }

    @Step("verify avatar img")
    public void verifyAvatarImg(BufferedImage expectedImg) {
        try {
            BufferedImage actualImage = ImageIO.read(Objects.requireNonNull(self.screenshot()));
            boolean hasDiff = new ScreenDiffResult(expectedImg, actualImage).getAsBoolean();
            Assertions.assertFalse(hasDiff, "screenshot did not match");
        } catch (IOException e) {
            throw new RuntimeException("Couldn't read avatar element screenshot", e);
        }
    }
}

