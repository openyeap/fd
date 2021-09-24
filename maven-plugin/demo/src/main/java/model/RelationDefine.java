package model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class RelationDefine implements Serializable {
    Type name;
    Class fromEntity;
    String fromField;
    Class toEntity;
    String toField;

    public enum Type {
        One2One,
        One2Many,
        Many2One,
        Many2Many,
    }
}
