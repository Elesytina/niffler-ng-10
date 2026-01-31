package guru.qa.niffler.jupiter.extension;

import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.TestResult;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

public class AllureBackendLogsExtension implements SuiteExtension {

    public static String caseName = "Niffler backend logs";

    @Override
    public void afterSuite() {
        final AllureLifecycle lifecycle = Allure.getLifecycle();

        final String caseId = UUID.randomUUID().toString();

        lifecycle.scheduleTestCase(new TestResult().setUuid(caseId).setName(caseName));
        lifecycle.startTestCase(caseId);

        lifecycle.stopTestCase(caseId);
        lifecycle.writeTestCase(caseId);
        lifecycle.addAttachment("Niffler-auth logs",
                "text/html",
                ".log",
                Files.newInputStream(Path.of("/logs/niffler-auth/app.log"));
    }
}
