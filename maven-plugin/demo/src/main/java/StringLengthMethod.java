import freemarker.template.*;
import lombok.extern.slf4j.Slf4j;


import java.util.List;

@Slf4j
public class StringLengthMethod implements TemplateMethodModelEx {
    public TemplateModel exec(List args) throws TemplateModelException {
        if (args.size() != 1) {
            throw new TemplateModelException("Wrong arguments");
        }
        return new SimpleNumber(((String) args.get(0)).length());
    }
}


