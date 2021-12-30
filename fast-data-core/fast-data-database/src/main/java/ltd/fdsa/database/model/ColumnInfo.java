package ltd.fdsa.database.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ColumnInfo {
    String name;
    String alias;
    String code;
    boolean numerical;
}

