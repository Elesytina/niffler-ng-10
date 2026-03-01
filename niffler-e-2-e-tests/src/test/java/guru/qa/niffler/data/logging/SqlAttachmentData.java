package guru.qa.niffler.data.logging;

import io.qameta.allure.attachment.AttachmentData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SqlAttachmentData implements AttachmentData {

    private final String name;
    private final String sql;

}
