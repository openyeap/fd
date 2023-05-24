package cn.zhumingwu.database.sql.testsupport;

import lombok.var;
import cn.zhumingwu.database.sql.domain.BuildingContext;
import cn.zhumingwu.database.sql.functions.Function;
import cn.zhumingwu.database.sql.utils.Indentation;
import org.assertj.core.api.AbstractStringAssert;

import static cn.zhumingwu.database.sql.dialect.Dialects.MYSQL;
import static org.assertj.core.api.Assertions.assertThat;

public interface FunctionAssert
{
    default AbstractStringAssert<?> assertFunction(Function function)
    {
        var indentation = Indentation.indent(false);
        return assertThat(function.getValue(new BuildingContext(MYSQL, indentation.getDelimiter()), indentation));
    }
}
