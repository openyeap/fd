package cn.zhumingwu.starter.jdbc.test;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class Entity extends BaseEntity<Integer> {
    String name;
    int age;
    Date date;
}