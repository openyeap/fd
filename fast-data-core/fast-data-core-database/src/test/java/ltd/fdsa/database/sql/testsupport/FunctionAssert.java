package ltd.fdsa.database.sql.testsupport;

import static ltd.fdsa.database.sql.dialect.Dialects.MYSQL;
import static org.assertj.core.api.Assertions.assertThat;

import lombok.var;
import ltd.fdsa.database.sql.domain.BuildingContext;
import ltd.fdsa.database.sql.functions.Function;
import ltd.fdsa.database.sql.utils.Indentation;
import org.assertj.core.api.AbstractStringAssert;

public interface FunctionAssert
{
    default AbstractStringAssert<?> assertFunction(Function function)
    {
        var indentation = Indentation.indent(false);
        return assertThat(function.getValue(new BuildingContext(MYSQL, indentation.getDelimiter()), indentation));
    }
}
