package ltd.fdsa.fql.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FilterInfo {
    private String name;
    private FilterSet.Type type;
    private Object value;
}
