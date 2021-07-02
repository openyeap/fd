package ltd.fdsa.starter.jdbc.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public
class DBResult {
    private String name;
    private Object content;
}
