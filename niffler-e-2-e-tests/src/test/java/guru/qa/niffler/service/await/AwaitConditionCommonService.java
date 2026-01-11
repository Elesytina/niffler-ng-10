package guru.qa.niffler.service.await;

import io.qameta.allure.Step;
import org.awaitility.core.ConditionTimeoutException;

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
}
