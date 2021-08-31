package ltd.fdsa.maven.codegg.model;

import lombok.Data;
import ltd.fdsa.maven.codegg.model.Entity;

@Data
public class Module {
    String name;
    String description;
    Entity[] entities;
}
