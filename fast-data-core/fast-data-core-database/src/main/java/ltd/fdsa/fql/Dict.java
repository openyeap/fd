package ltd.fdsa.fql;

import java.util.Map;
import java.util.Properties;

public class Dict extends Properties {

    public String getDict(String key) {
       return this.getProperty(key,key);
    }
}
