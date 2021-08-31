package ltd.fdsa.maven.codegg.model;

import lombok.Data;

@Data
public class Field {
    String name;
    String code;
    String remark;
    boolean isNull;
    boolean autoIncrement;
    String type;
    int length;
    int scale;
}
