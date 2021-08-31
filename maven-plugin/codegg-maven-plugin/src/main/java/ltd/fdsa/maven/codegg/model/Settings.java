package ltd.fdsa.maven.codegg.model;

import lombok.Data;

import javax.activation.DataSource;
import java.util.Map;

@Data
public class Settings {
    Map<String, Map<String, String>> dataType;
    Map<String, String> template;
    DataSource[] dataSources;
}
