import lombok.Data;
import annotation.IsNull;

@Data
public class User implements IEntity {
    @IsNull
    String Name;
}

