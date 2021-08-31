package ltd.fdsa.maven.codegg.model;

import lombok.Data;

@Data
public class Entity {
    String name;
    String code;
    String remark;

    Field[] fields;
}
