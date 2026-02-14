package guru.qa.niffler.jupiter.extension;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.qa.niffler.exception.ScreenshotException;
import guru.qa.niffler.jupiter.annotation.ScreenshotTest;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.junit.platform.commons.support.AnnotationSupport;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

@ParametersAreNonnullByDefault
public class ScreenShotTestExtension implements ParameterResolver, TestExecutionExceptionHandler {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(ScreenShotTestExtension.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Base64.Encoder encoder = Base64.getEncoder();

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return AnnotationSupport.isAnnotated(extensionContext.getRequiredTestMethod(), ScreenshotTest.class) &&
                parameterContext.getParameter().getType().isAssignableFrom(BufferedImage.class);
    }

    @Override
    public BufferedImage resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        try {
            final String expectedScreenFilePath = extensionContext
                    .getRequiredTestMethod()
                    .getAnnotation(ScreenshotTest.class).value();
            return ImageIO.read(
                    new ClassPathResource(expectedScreenFilePath)
                            .getInputStream()
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to read EXPECTED screenshot", e);
        }
    }

    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        if (throwable instanceof ScreenshotException) {
            byte[] actual = imageToBytes(getActual());
            byte[] expected = imageToBytes(getExpected());
            byte[] diff = imageToBytes(getDiff());

            attachDiffScreenshots(actual, expected, diff);
        }
        ScreenshotTest screenshotTest = context.getRequiredTestMethod().getAnnotation(ScreenshotTest.class);

        if (screenshotTest.rewriteExpected()) {
            final BufferedImage actual = getActual();
            ImageIO.write(
                    actual,
                    "png",
                    new File("src/test/resources/" + screenshotTest.value())
            );
        }
        throw throwable;
    }

    public static void setExpected(BufferedImage expected) {
        TestMethodContextExtension.context().getStore(NAMESPACE).put("expected", expected);
    }

    public static @Nonnull BufferedImage getExpected() {
        return TestMethodContextExtension.context().getStore(NAMESPACE).get("expected", BufferedImage.class);
    }

    public static void setActual(BufferedImage actual) {
        TestMethodContextExtension.context().getStore(NAMESPACE).put("actual", actual);
    }

    public static @Nonnull BufferedImage getActual() {
        return TestMethodContextExtension.context().getStore(NAMESPACE).get("actual", BufferedImage.class);
    }

    public static void setDiff(BufferedImage diff) {
        TestMethodContextExtension.context().getStore(NAMESPACE).put("diff", diff);
    }

    public static BufferedImage getDiff() {
        return TestMethodContextExtension.context().getStore(NAMESPACE).get("diff", BufferedImage.class);
    }

    private static @Nonnull byte[] imageToBytes(BufferedImage image) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", outputStream);

            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void attachDiffScreenshots(byte[] actual, byte[] expected, byte[] diff) {
        Allure.addAttachment("Actual Screenshot", "image/png", new ByteArrayInputStream(actual), ".png");
        Allure.addAttachment("Expected Screenshot", "image/png", new ByteArrayInputStream(expected), ".png");
        Allure.addAttachment("Diff Screenshot", "image/png", new ByteArrayInputStream(diff), ".png");
    }
}