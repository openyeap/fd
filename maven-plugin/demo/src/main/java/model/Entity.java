package model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class Entity  implements Serializable {
    String name;
    String code;
    String remark;
    Field[] fields;
}
