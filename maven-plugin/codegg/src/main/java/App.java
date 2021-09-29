import annotation.Column;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import lombok.var;

import java.io.IOException;
import java.io.StringWriter;


@Slf4j
public class App {
    public static void main(String[] args) throws IOException, TemplateException {

        Egg egg = new Egg();
        egg.execute();
    }
}


