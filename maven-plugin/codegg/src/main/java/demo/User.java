package demo;

import annotation.Column;
import lombok.Data;

@Data
public class User implements IEntity {
    @Column
    String name;
}

