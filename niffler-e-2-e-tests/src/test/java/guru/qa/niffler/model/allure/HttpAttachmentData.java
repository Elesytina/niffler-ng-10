package guru.qa.niffler.model.allure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.attachment.AttachmentData;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
@Data
public class HttpAttachmentData implements AttachmentData {

    private String url;
    private String name;
    private String contentType;
    private String method;
    private String body;
    private int responseCode;
    private Map<String, String> headers;
    private Map<String, String> cookies;

    public HttpAttachmentDataBuilder builder() {
        return new HttpAttachmentDataBuilder();
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public class HttpAttachmentDataBuilder {

        public HttpAttachmentDataBuilder withBody(String body) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                Object json = mapper.readValue(body, Object.class);
                body = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
                HttpAttachmentData.this.body = body;
            } catch (JsonProcessingException e) {
                HttpAttachmentData.this.body = body;
            }
            return this;
        }

        public HttpAttachmentDataBuilder withName(String name) {
            HttpAttachmentData.this.name = name;
            return this;
        }

        public HttpAttachmentDataBuilder withContentType(String contentType) {
            HttpAttachmentData.this.contentType = contentType;
            return this;
        }

        public HttpAttachmentDataBuilder withUrl(String url) {
            HttpAttachmentData.this.url = url;
            return this;
        }

        public HttpAttachmentDataBuilder withMethod(String method) {
            HttpAttachmentData.this.method = method;
            return this;
        }

        public HttpAttachmentDataBuilder withResponseCode(int responseCode) {
            HttpAttachmentData.this.responseCode = responseCode;
            return this;
        }

        public HttpAttachmentDataBuilder withHeaders(Map<String, String> headers) {
            HttpAttachmentData.this.headers = headers;
            return this;
        }

        public HttpAttachmentDataBuilder withCookies(Map<String, String> cookies) {
            HttpAttachmentData.this.cookies = cookies;
            return this;
        }

        public HttpAttachmentData build() {
            return HttpAttachmentData.this;
        }
    }
}
