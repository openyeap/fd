package function;
import freemarker.template.SimpleNumber;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import lombok.extern.slf4j.Slf4j;
import lombok.var;

import java.util.List;

@Slf4j
public class StringLengthMethod implements TemplateMethodModelEx {
    public TemplateModel exec(List args) throws TemplateModelException {
        if (args.size() != 1) {
            throw new TemplateModelException("Wrong arguments");
        }
        var input = args.get(0).toString();
        var length = input.length();
        return new SimpleNumber(length);
    }
}


