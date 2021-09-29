import annotation.DD;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.StringWriter;


@Slf4j
public class App {
    public static void main(String[] args) throws IOException, TemplateException {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_0);

        Template fileNameTemplate = new Template("file", "${columns}", configuration);
        StringWriter result = new StringWriter();


        fileNameTemplate.process(new DD(), result);

        Egg egg = new Egg();
        egg.execute();
    }
}


