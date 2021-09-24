package model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class Field implements Serializable {
    String name;
    String code;
    String remark;
    boolean isNull;
    boolean autoIncrement;
    String type;
    int length;
    int scale;
}
