import annotation.Relation;
import lombok.Data;

@Data
public class UserExt extends User {
    Integer age;
    @Relation(entity = User.class, field = "name")
    Integer id;
}

