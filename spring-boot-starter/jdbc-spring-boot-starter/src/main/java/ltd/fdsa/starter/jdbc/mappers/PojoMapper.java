package ltd.fdsa.starter.jdbc.mappers;

import lombok.SneakyThrows;
import lombok.var;
import org.springframework.jdbc.core.RowMapper;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PojoMapper<T> implements RowMapper<T> {

    private Class<T> clazz;

    public PojoMapper() {
        this.clazz = (Class<T>) ((ParameterizedType) (this.getClass().getGenericInterfaces()[0])).getActualTypeArguments()[0];
    }


    @Override
    @SneakyThrows
    public T mapRow(ResultSet resultSet, int rowNum) {
        T o = clazz.newInstance();
        for (var f : clazz.getFields()) {
            var name = f.getName();
            var value = resultSet.getObject(name);
            f.set(o, value);
        }
        return o;
    }
}
