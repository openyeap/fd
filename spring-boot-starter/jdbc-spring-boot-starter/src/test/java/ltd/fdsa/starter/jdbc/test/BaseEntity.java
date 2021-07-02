package ltd.fdsa.starter.jdbc.test;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class BaseEntity<ID> {
    ID id;
    Date createTime;
}