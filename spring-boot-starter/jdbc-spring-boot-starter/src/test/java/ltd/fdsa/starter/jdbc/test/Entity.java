package ltd.fdsa.starter.jdbc.test;

import lombok.Data;
import lombok.ToString;
import ltd.fdsa.starter.jdbc.MyA;

import java.util.Date;

@Data
@ToString
@MyA(name = "myname", value = "myvalue")
public class Entity extends BaseEntity<Integer> {

    String name;
    int age;
    Date date;
}