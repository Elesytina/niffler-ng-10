package guru.qa.niffler.utils;

import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;

import java.awt.image.BufferedImage;
import java.util.function.BooleanSupplier;

import static guru.qa.niffler.jupiter.extension.ScreenShotTestExtension.setActual;
import static guru.qa.niffler.jupiter.extension.ScreenShotTestExtension.setDiff;
import static guru.qa.niffler.jupiter.extension.ScreenShotTestExtension.setExpected;

public class ScreenDiffResult implements BooleanSupplier {

    private final BufferedImage expected;
    private final BufferedImage actual;
    private final ImageDiff diff;
    private final boolean hasDiff;

    public ScreenDiffResult(BufferedImage expected, BufferedImage actual) {
        this.expected = expected;
        this.actual = actual;
        this.diff = new ImageDiffer().makeDiff(expected, actual);
        this.hasDiff = diff.hasDiff();
    }

    @Override
    public boolean getAsBoolean() {
        if (hasDiff) {
            setActual(actual);
            setExpected(expected);
            setDiff(diff.getMarkedImage());
        }

        return hasDiff;
    }
}
