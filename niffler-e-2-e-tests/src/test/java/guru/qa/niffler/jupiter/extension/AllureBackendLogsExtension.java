package guru.qa.niffler.jupiter.extension;

import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.TestResult;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.UUID;

@ParametersAreNonnullByDefault
public class AllureBackendLogsExtension implements SuiteExtension {

    private static final String caseName = "Niffler backend logs";
    private static final Set<String> services = Set.of(
            "niffler-auth",
            "niffler-currency",
            "niffler-gateway",
            "niffler-spend",
            "niffler-userdata"
    );

    @Override
    public void afterSuite() {
        final AllureLifecycle allureLifecycle = Allure.getLifecycle();
        final String caseId = UUID.randomUUID().toString();
        allureLifecycle.scheduleTestCase(new TestResult().setUuid(caseId).setName(caseName));
        allureLifecycle.startTestCase(caseId);

        services.forEach(service -> {
            try {
                addAttachmentForService(allureLifecycle, service);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        allureLifecycle.stopTestCase(caseId);
        allureLifecycle.writeTestCase(caseId);
    }

    private static void addAttachmentForService(AllureLifecycle allureLifecycle, String serviceName) throws IOException {
        allureLifecycle.addAttachment(
                serviceName + " log",
                "text/html",
                ".log",
                Files.newInputStream(
                        Path.of("./logs/" + serviceName + "/app.log")
                )
        );
    }
}