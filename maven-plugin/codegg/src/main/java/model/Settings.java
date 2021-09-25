package model;

import lombok.Data;

import javax.activation.DataSource;
import java.io.Serializable;
import java.util.Map;

@Data
public class Settings implements Serializable {
    Map<String, Map<String, String>> dataType;
    Map<String, String> template;
    DataSource[] dataSources;
}
