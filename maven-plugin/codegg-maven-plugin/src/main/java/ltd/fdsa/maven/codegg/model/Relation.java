package ltd.fdsa.maven.codegg.model;

import lombok.Data;

@Data
public class Relation {
    String name;
    RelationDefine from;
    RelationDefine to;
}

