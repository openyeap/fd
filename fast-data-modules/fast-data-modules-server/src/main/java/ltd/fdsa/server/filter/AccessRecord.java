package ltd.fdsa.server.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;

@Data
@Slf4j
public class AccessRecord {
    String path;
    HttpHeaders headers;
    String method;
    String schema;

    String remoteAddress;
    String targetUri;
    MultiValueMap<String, String> formData;
    String body;

}