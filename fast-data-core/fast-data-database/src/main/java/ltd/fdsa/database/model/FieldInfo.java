package ltd.fdsa.database.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FieldInfo {
    String name;
    Class<?> type;
    boolean unique;
    boolean nullable;
    int length;
    int precision;
    int scale;
}