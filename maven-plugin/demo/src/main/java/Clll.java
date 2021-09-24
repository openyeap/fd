import lombok.Data;
import annotation.IsNull;

@Data
public class User {
    @IsNull
    String Name;
}