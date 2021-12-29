package ltd.fdsa.starter.jdbc.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) 
public class ViewUpdate {
    private Map<String, Object> data;
    private Map<String, Object> where;
}
