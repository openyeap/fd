package ltd.fdsa.fql;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NameAndAlias {
    String name;
    String alias;
}
