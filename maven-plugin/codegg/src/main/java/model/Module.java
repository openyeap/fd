package model;

import lombok.Builder;
import lombok.Data;
import model.Entity;

import java.io.Serializable;

@Data
@Builder
public class Module  implements Serializable {
    String name;
    String description;
    Entity[] entities;
    RelationDefine[] relations;
}
