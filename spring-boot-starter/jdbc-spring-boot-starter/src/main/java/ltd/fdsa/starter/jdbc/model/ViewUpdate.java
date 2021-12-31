package ltd.fdsa.starter.jdbc.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) 
public class ViewUpdate {
    private Map<String, Object> data;
    private Map<String, Object> where;
}
