package guru.qa.niffler.service.await;

import io.qameta.allure.Step;
import org.awaitility.core.ConditionTimeoutException;

import javax.annotation.Nonnull;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import static java.time.Duration.ofSeconds;
import static org.awaitility.Awaitility.await;

public class AwaitConditionCommonService {

    @Step("await for condition")
    public static void awaitForCondition(Supplier<Boolean> condition) {
        try {
            await().atMost(ofSeconds(5))
                    .pollInterval(ofSeconds(1))
                    .until(condition::get);
        } catch (ConditionTimeoutException e) {
            throw new AssertionError("Couldn't await for condition");
        }
    }

    @Nonnull
    @Step("await for event and get pojo")
    public static <T> T awaitForEvent(Supplier<T> supplier) {
        AtomicReference<T> result = new AtomicReference<>();
        try {
            await().atMost(ofSeconds(5))
                    .pollInterval(ofSeconds(1))
                    .until(() -> {
                        result.set(supplier.get());

                        return result.get() != null;
                    });
            return result.get();
        } catch (ConditionTimeoutException e) {
            throw new AssertionError("Couldn't await for condition");
        }
    }
}
